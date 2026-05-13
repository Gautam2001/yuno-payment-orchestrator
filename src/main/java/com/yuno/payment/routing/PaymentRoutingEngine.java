package com.yuno.payment.routing;

import org.springframework.stereotype.Component;

import com.yuno.payment.connector.PaymentProviderConnector;
import com.yuno.payment.connector.provider.ProviderAConnector;
import com.yuno.payment.connector.provider.ProviderBConnector;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentRoutingEngine {

	private final ProviderAConnector providerAConnector;

	private final ProviderBConnector providerBConnector;

	public PaymentProviderConnector getPrimaryProvider(String paymentMethod) {

		if ("CARD".equalsIgnoreCase(paymentMethod)) {
			return providerAConnector;
		}

		if ("UPI".equalsIgnoreCase(paymentMethod)) {
			return providerBConnector;
		}

		throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
	}

	public PaymentProviderConnector getFailoverProvider(String paymentMethod) {

		if ("CARD".equalsIgnoreCase(paymentMethod)) {
			return providerBConnector;
		}

		if ("UPI".equalsIgnoreCase(paymentMethod)) {
			return providerAConnector;
		}

		throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
	}
}