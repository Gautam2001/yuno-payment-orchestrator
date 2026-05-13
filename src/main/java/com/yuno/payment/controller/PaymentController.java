package com.yuno.payment.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.PaymentResponse;
import com.yuno.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "Create Payment", description = "Creates and processes a payment using the orchestration engine with routing, retries, failover, and idempotency support.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse createPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
			@Valid @RequestBody CreatePaymentRequest request) {
		return paymentService.createPayment(idempotencyKey, request);
	}

	@Operation(summary = "Fetch Payment", description = "Fetch payment details by payment ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Payment found"),
			@ApiResponse(responseCode = "404", description = "Payment not found") })
	@GetMapping("/{paymentId}")
	public PaymentResponse getPayment(@PathVariable UUID paymentId) {
		return paymentService.getPayment(paymentId);
	}

	@Operation(summary = "Fetch All Payments", description = "Returns all payments with FAILED payments prioritized before SUCCESS payments.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Payments fetched successfully") })
	@GetMapping("payments")
	public List<PaymentResponse> getAllPayments() {
		return paymentService.getAllPayments();
	}
}