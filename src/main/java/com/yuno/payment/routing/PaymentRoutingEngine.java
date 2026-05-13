package com.yuno.payment.routing;

import com.yuno.payment.connector.PaymentProviderConnector;
import com.yuno.payment.connector.provider.ProviderAConnector;
import com.yuno.payment.connector.provider.ProviderBConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRoutingEngine {

    private final ProviderAConnector providerAConnector;

    private final ProviderBConnector providerBConnector;

    public PaymentProviderConnector route(String paymentMethod) {

        if("CARD".equalsIgnoreCase(paymentMethod)) {
            return providerAConnector;
        }

        if("UPI".equalsIgnoreCase(paymentMethod)) {
            return providerBConnector;
        }

        throw new IllegalArgumentException(
                "Unsupported payment method: " + paymentMethod
        );
    }
}