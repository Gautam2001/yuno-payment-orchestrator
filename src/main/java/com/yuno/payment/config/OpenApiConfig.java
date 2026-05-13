package com.yuno.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customOpenAPI() {

		return new OpenAPI().info(new Info().title("Yuno Payment Orchestration API").version("1.0").description(
				"Simplified payment orchestration system with routing, retries, failover, idempotency, and payment tracking.")
				.contact(new Contact().name("Gautam Singhal").email("singhal.gautam.gs@gmail.com@gmail.com")));
	}
}