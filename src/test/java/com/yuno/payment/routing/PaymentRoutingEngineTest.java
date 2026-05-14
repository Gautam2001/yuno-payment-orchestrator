package com.yuno.payment.routing;

import com.yuno.payment.connector.provider.ProviderAConnector;
import com.yuno.payment.connector.provider.ProviderBConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRoutingEngineTest {

    private PaymentRoutingEngine routingEngine;

    @BeforeEach
    void setUp() {

        routingEngine =
                new PaymentRoutingEngine(
                        new ProviderAConnector(),
                        new ProviderBConnector()
                );
    }

    @Test
    void shouldRouteCardPaymentsToProviderA() {

        String provider =
                routingEngine
                        .getPrimaryProvider("CARD")
                        .getProviderName();

        assertEquals(
                "PROVIDER_A (CARD)",
                provider
        );
    }

    @Test
    void shouldRouteUpiPaymentsToProviderB() {

        String provider =
                routingEngine
                        .getPrimaryProvider("UPI")
                        .getProviderName();

        assertEquals(
                "PROVIDER_B (UPI)",
                provider
        );
    }

    @Test
    void shouldReturnFailoverProvider() {

        String provider =
                routingEngine
                        .getFailoverProvider("CARD")
                        .getProviderName();

        assertEquals(
                "PROVIDER_B (UPI)",
                provider
        );
    }

    @Test
    void shouldThrowExceptionForInvalidMethod() {

        assertThrows(
                IllegalArgumentException.class,
                () -> routingEngine
                        .getPrimaryProvider("BITCOIN")
        );
    }
}