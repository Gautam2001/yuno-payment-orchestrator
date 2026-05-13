package com.yuno.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderResponse {

    private boolean success;

    private String transactionId;

    private String message;
}