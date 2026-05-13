package com.yuno.payment.connector.provider;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.yuno.payment.connector.PaymentProviderConnector;
import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.ProviderResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProviderBConnector implements PaymentProviderConnector {

	@Override
	public ProviderResponse processPayment(CreatePaymentRequest request) {

		log.info("Processing payment through PROVIDER_B (UPI)");

		simulateDelay(1200);

		boolean success = Math.random() < 0.6;

		if (success) {

			return ProviderResponse.builder().success(true).transactionId(UUID.randomUUID().toString())
					.message("Payment processed successfully by Provider B (UPI)").build();
		}

		return ProviderResponse.builder().success(false).message("Provider B (UPI) payment failed").build();
	}

	@Override
	public String getProviderName() {
		return "PROVIDER_B (UPI)";
	}

	private void simulateDelay(long millis) {

		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}