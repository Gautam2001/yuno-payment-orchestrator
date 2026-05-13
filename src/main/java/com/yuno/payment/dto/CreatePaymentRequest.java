package com.yuno.payment.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "Payment amount", example = "1500")
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.0", message = "Amount must be greater than 0")
	private BigDecimal amount;

	@Schema(description = "Currency code", example = "INR")
	@NotBlank(message = "Currency is required")
	private String currency;

	@Schema(description = "Payment method", example = "UPI")
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
}