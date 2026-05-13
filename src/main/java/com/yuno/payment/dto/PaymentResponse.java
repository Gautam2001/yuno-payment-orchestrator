package com.yuno.payment.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

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

    private String status;

    private String provider;
}