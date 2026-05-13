package com.yuno.payment.service;

import java.util.List;
import java.util.UUID;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;

public interface PaymentService {

	PaymentResponse createPayment(String idempotencyKey, CreatePaymentRequest request);

	PaymentResponse getPayment(UUID paymentId);

	List<PaymentResponse> getAllPayments();
}