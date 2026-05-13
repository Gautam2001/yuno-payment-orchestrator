package com.yuno.payment.connector.provider;

import com.yuno.payment.connector.PaymentProviderConnector;
import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.ProviderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ProviderAConnector implements PaymentProviderConnector {

    @Override
    public ProviderResponse processPayment(CreatePaymentRequest request) {

        log.info("Processing payment through PROVIDER_A");

        boolean success = Math.random() < 0.8;

        if(success) {

            return ProviderResponse.builder()
                    .success(true)
                    .transactionId(UUID.randomUUID().toString())
                    .message("Payment processed successfully by Provider A")
                    .build();
        }

        return ProviderResponse.builder()
                .success(false)
                .message("Provider A payment failed")
                .build();
    }

    @Override
    public String getProviderName() {
        return "PROVIDER_A";
    }
}