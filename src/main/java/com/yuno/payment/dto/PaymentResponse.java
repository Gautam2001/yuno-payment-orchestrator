package com.yuno.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

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
public class PaymentResponse {

	private UUID paymentId;

	private BigDecimal amount;

	private String currency;

	private String paymentMethod;

	private String paymentStatus;

	private String finalProvider;

	private String providerTransactionId;

	private Integer retryCount;

	private Boolean failoverOccurred;

	private Long processingTimeMs;

	private String finalFailureReason;

	private String attemptHistory;
}