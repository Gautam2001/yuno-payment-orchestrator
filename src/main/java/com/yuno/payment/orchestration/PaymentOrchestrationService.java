package com.yuno.payment.orchestration;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrchestrationService {

    private final PaymentRoutingEngine routingEngine;

    private final PaymentRepository paymentRepository;

    public PaymentResponse processPayment(CreatePaymentRequest request) {

        log.info("Starting payment orchestration");

        PaymentProviderConnector connector =
                routingEngine.route(request.getPaymentMethod());

        log.info("Selected provider: {}", connector.getProviderName());

        ProviderResponse providerResponse =
                connector.processPayment(request);

        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .provider(connector.getProviderName())
                .providerTransactionId(
                        providerResponse.getTransactionId()
                )
                .failureReason(
                        providerResponse.isSuccess()
                                ? null
                                : providerResponse.getMessage()
                )
                .status(
                        providerResponse.isSuccess()
                                ? PaymentStatus.SUCCESS
                                : PaymentStatus.FAILED
                )
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment processed with status: {}",
                savedPayment.getStatus());

        return PaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .paymentMethod(savedPayment.getPaymentMethod())
                .status(savedPayment.getStatus().name())
                .provider(savedPayment.getProvider())
                .build();
    }
}