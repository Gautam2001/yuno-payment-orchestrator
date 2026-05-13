package com.yuno.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String currency;

	@Column(nullable = false)
	private String paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	private String provider;

	private String providerTransactionId;

	private Integer retryCount;

	private Boolean failoverOccurred;

	private String finalFailureReason;

	@Column(length = 5000)
	private String attemptHistory;

	private Long processingTimeMs;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}