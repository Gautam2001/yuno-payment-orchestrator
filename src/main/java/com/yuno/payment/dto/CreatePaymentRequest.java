package com.yuno.payment.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.0", message = "Amount must be greater than 0")
	private BigDecimal amount;

	@NotBlank(message = "Currency is required")
	private String currency;

	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
}