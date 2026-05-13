package com.yuno.payment.service.impl;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.entity.Payment;
import com.yuno.payment.exception.PaymentNotFoundException;
import com.yuno.payment.orchestration.PaymentOrchestrationService;
import com.yuno.payment.repository.PaymentRepository;
import com.yuno.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with id: " + paymentId
                        ));

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus().name())
                .provider(payment.getProvider())
                .build();
    }
}