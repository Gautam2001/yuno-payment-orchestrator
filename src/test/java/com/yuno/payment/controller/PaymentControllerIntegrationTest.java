package com.yuno.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuno.payment.dto.CreatePaymentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Test
    void shouldCreatePaymentSuccessfully()
            throws Exception {

        CreatePaymentRequest request =
                CreatePaymentRequest.builder()
                        .amount(BigDecimal.valueOf(1500))
                        .currency("INR")
                        .paymentMethod("UPI")
                        .build();

        mockMvc.perform(
                        post("/payments")
                                .header(
                                        "Idempotency-Key",
                                        "abc123"
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.paymentStatus")
                                .exists()
                );
    }

    @Test
    void shouldReturn400ForInvalidRequest()
            throws Exception {

        CreatePaymentRequest request =
                CreatePaymentRequest.builder()
                        .amount(BigDecimal.ZERO)
                        .currency("")
                        .paymentMethod("")
                        .build();

        mockMvc.perform(
                        post("/payments")
                                .header(
                                        "Idempotency-Key",
                                        "invalid123"
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }
}