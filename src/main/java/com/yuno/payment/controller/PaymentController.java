package com.yuno.payment.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
		return paymentService.createPayment(request);
	}

	@GetMapping("/{paymentId}")
	public PaymentResponse getPayment(@PathVariable UUID paymentId) {
		return paymentService.getPayment(paymentId);
	}

	@GetMapping("payments")
	public List<PaymentResponse> getAllPayments() {
		return paymentService.getAllPayments();
	}
}