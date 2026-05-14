package com.yuno.payment.service;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.entity.IdempotencyRecord;
import com.yuno.payment.entity.Payment;
import com.yuno.payment.entity.PaymentStatus;
import com.yuno.payment.exception.PaymentNotFoundException;
import com.yuno.payment.orchestration.PaymentOrchestrationService;
import com.yuno.payment.repository.IdempotencyRepository;
import com.yuno.payment.repository.PaymentRepository;
import com.yuno.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private IdempotencyRepository idempotencyRepository;

    @Mock
    private PaymentOrchestrationService orchestrationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {

        CreatePaymentRequest request =
                CreatePaymentRequest.builder()
                        .amount(BigDecimal.valueOf(1000))
                        .currency("INR")
                        .paymentMethod("UPI")
                        .build();

        PaymentResponse mockedResponse =
                PaymentResponse.builder()
                        .paymentId(UUID.randomUUID())
                        .paymentStatus("SUCCESS")
                        .finalProvider("PROVIDER_B")
                        .build();

        when(idempotencyRepository
                .findByIdempotencyKey("key-1"))
                .thenReturn(Optional.empty());

        when(orchestrationService
                .processPayment(request))
                .thenReturn(mockedResponse);

        PaymentResponse response =
                paymentService.createPayment(
                        "key-1",
                        request
                );

        assertEquals(
                "SUCCESS",
                response.getPaymentStatus()
        );
    }

    @Test
    void shouldReturnExistingPaymentForDuplicateKey() {

        UUID paymentId = UUID.randomUUID();

        IdempotencyRecord record =
                IdempotencyRecord.builder()
                        .idempotencyKey("duplicate")
                        .paymentId(paymentId)
                        .createdAt(LocalDateTime.now())
                        .build();

        Payment payment =
                Payment.builder()
                        .id(paymentId)
                        .amount(BigDecimal.valueOf(500))
                        .currency("INR")
                        .paymentMethod("CARD")
                        .status(PaymentStatus.SUCCESS)
                        .provider("PROVIDER_A")
                        .build();

        when(idempotencyRepository
                .findByIdempotencyKey("duplicate"))
                .thenReturn(Optional.of(record));

        when(paymentRepository.findById(paymentId))
                .thenReturn(Optional.of(payment));

        PaymentResponse response =
                paymentService.createPayment(
                        "duplicate",
                        CreatePaymentRequest.builder().build()
                );

        assertEquals(
                paymentId,
                response.getPaymentId()
        );

        verify(orchestrationService, never())
                .processPayment(any());
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {

        UUID paymentId = UUID.randomUUID();

        when(paymentRepository.findById(paymentId))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPayment(paymentId)
        );
    }
}