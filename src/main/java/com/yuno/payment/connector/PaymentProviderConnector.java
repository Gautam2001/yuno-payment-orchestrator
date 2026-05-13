package com.yuno.payment.connector;

import com.yuno.payment.dto.CreatePaymentRequest;
import com.yuno.payment.dto.ProviderResponse;

public interface PaymentProviderConnector {

	ProviderResponse processPayment(CreatePaymentRequest request);

	String getProviderName();
}