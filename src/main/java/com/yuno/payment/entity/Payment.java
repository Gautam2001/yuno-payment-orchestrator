package com.yuno.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

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
	private PaymentStatus status;

	private String provider;
	
	private String providerTransactionId;

    private String failureReason;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
