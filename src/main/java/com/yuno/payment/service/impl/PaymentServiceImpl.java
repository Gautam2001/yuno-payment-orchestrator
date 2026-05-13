package com.yuno.payment.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.entity.Payment;
import com.yuno.payment.exception.PaymentNotFoundException;
import com.yuno.payment.orchestration.PaymentOrchestrationService;
import com.yuno.payment.repository.PaymentRepository;
import com.yuno.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	private final PaymentOrchestrationService orchestrationService;

	@Override
	public PaymentResponse createPayment(CreatePaymentRequest request) {

		return orchestrationService.processPayment(request);
	}

	@Override
	public PaymentResponse getPayment(UUID paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));

		return mapToResponse(payment);
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
	
	@Override
	public List<PaymentResponse> getAllPayments() {

	    return paymentRepository
	            .findAllPaymentsOrderByFailurePriority()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
}