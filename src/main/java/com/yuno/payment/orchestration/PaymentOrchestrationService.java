package com.yuno.payment.orchestration;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.yuno.payment.connector.PaymentProviderConnector;
import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.dto.ProviderResponse;
import com.yuno.payment.entity.Payment;
import com.yuno.payment.entity.PaymentStatus;
import com.yuno.payment.repository.PaymentRepository;
import com.yuno.payment.routing.PaymentRoutingEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrchestrationService {

	private static final int MAX_RETRIES = 2;

	private final PaymentRoutingEngine routingEngine;

	private final PaymentRepository paymentRepository;

	public PaymentResponse processPayment(CreatePaymentRequest request) {

		long startTime = System.currentTimeMillis();

		StringBuilder attemptHistory = new StringBuilder();

		int retryCount = 0;

		boolean paymentSuccess = false;

		boolean failoverOccurred = false;

		ProviderResponse providerResponse = null;

		PaymentProviderConnector finalProvider = null;

		log.info("Starting payment orchestration | method={} amount={} currency={}", request.getPaymentMethod(),
				request.getAmount(), request.getCurrency());

		PaymentProviderConnector primaryProvider = routingEngine.getPrimaryProvider(request.getPaymentMethod());

		finalProvider = primaryProvider;

		while (retryCount <= MAX_RETRIES) {

			providerResponse = primaryProvider.processPayment(request);

			if (providerResponse.isSuccess()) {

				paymentSuccess = true;

				attemptHistory.append("SUCCESS -> ").append(primaryProvider.getProviderName()).append(" on attempt ")
						.append(retryCount + 1);

				break;
			}

			retryCount++;

			attemptHistory.append("FAILED -> ").append(primaryProvider.getProviderName()).append(" on attempt ")
					.append(retryCount).append(" | Reason: ").append(providerResponse.getMessage()).append(" || ");

			log.warn("Retry failed | provider={} retryCount={} reason={}", primaryProvider.getProviderName(),
					retryCount, providerResponse.getMessage());
		}

		// FAILOVER FLOW
		if (!paymentSuccess) {

			failoverOccurred = true;

			PaymentProviderConnector failoverProvider = routingEngine.getFailoverProvider(request.getPaymentMethod());

			finalProvider = failoverProvider;

			log.warn("Failover triggered | switchingTo={}", failoverProvider.getProviderName());

			providerResponse = failoverProvider.processPayment(request);

			if (providerResponse.isSuccess()) {

				paymentSuccess = true;

				attemptHistory.append("FAILOVER SUCCESS -> ").append(failoverProvider.getProviderName());

			} else {

				attemptHistory.append("FAILOVER FAILED -> ").append(failoverProvider.getProviderName())
						.append(" | Reason: ").append(providerResponse.getMessage());
			}
		}

		long processingTime = System.currentTimeMillis() - startTime;

		Payment payment = Payment.builder().amount(request.getAmount()).currency(request.getCurrency())
				.paymentMethod(request.getPaymentMethod())
				.status(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
				.provider(finalProvider.getProviderName())
				.providerTransactionId(providerResponse != null ? providerResponse.getTransactionId() : null)
				.retryCount(retryCount).failoverOccurred(failoverOccurred).attemptHistory(attemptHistory.toString())
				.finalFailureReason(paymentSuccess ? null : "Payment failed after retries and failover")
				.processingTimeMs(processingTime).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		Payment savedPayment = paymentRepository.save(payment);

		log.info("Payment completed | paymentId={} status={} provider={} processingTime={}ms", savedPayment.getId(),
				savedPayment.getStatus(), savedPayment.getProvider(), savedPayment.getProcessingTimeMs());

		return mapToResponse(savedPayment);
	}

	private PaymentResponse mapToResponse(Payment payment) {

		return PaymentResponse.builder().paymentId(payment.getId()).amount(payment.getAmount())
				.currency(payment.getCurrency()).paymentMethod(payment.getPaymentMethod())
				.paymentStatus(payment.getStatus().name()).finalProvider(payment.getProvider())
				.providerTransactionId(payment.getProviderTransactionId()).retryCount(payment.getRetryCount())
				.failoverOccurred(payment.getFailoverOccurred()).processingTimeMs(payment.getProcessingTimeMs())
				.finalFailureReason(payment.getFinalFailureReason()).attemptHistory(payment.getAttemptHistory())
				.build();
	}
}