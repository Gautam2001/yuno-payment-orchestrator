package com.yuno.payment.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.entity.IdempotencyRecord;
import com.yuno.payment.entity.Payment;
import com.yuno.payment.exception.PaymentNotFoundException;
import com.yuno.payment.orchestration.PaymentOrchestrationService;
import com.yuno.payment.repository.IdempotencyRepository;
import com.yuno.payment.repository.PaymentRepository;
import com.yuno.payment.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	private final PaymentOrchestrationService orchestrationService;

	private final IdempotencyRepository idempotencyRepository;

	@Override
	@Transactional
	public PaymentResponse createPayment(String idempotencyKey, CreatePaymentRequest request) {

		if (idempotencyKey.isBlank()) {

			throw new IllegalArgumentException("Idempotency-Key cannot be blank");
		}

		// Check existing idempotency key
		Optional<IdempotencyRecord> existingRecord = idempotencyRepository.findByIdempotencyKey(idempotencyKey);

		if (existingRecord.isPresent()) {

			log.info("Idempotency hit detected | key={}", idempotencyKey);

			UUID existingPaymentId = existingRecord.get().getPaymentId();

			Payment existingPayment = paymentRepository.findById(existingPaymentId)
					.orElseThrow(() -> new RuntimeException("Existing payment not found"));

			return mapToResponse(existingPayment);
		}

		// Process payment normally
		PaymentResponse response = orchestrationService.processPayment(request);

		IdempotencyRecord record = IdempotencyRecord.builder().idempotencyKey(idempotencyKey)
				.paymentId(response.getPaymentId()).createdAt(LocalDateTime.now()).build();

		idempotencyRepository.save(record);

		return response;
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

		return paymentRepository.findAllPaymentsOrderByFailurePriority().stream().map(this::mapToResponse).toList();
	}
}