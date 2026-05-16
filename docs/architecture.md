# Architecture Overview

## Objective

The objective of this project is to simulate a simplified payment orchestration platform similar to systems used by payment infrastructure providers.

The application accepts payment requests, routes them to appropriate providers based on payment method, retries failed requests, performs provider failover when necessary, and maintains payment state persistence.

---

## High Level Flow
Client
   ↓
Controller Layer
   ↓
Service Layer (Orchestration Engine)
   ↓
Routing Engine
   ↓
Provider Connectors
   ↓
Persistence Layer (PostgreSQL)

---

## Architecture Components

### 1. Controller Layer

The controller layer exposes REST APIs for external consumers.

Responsibilities:
- Accept incoming payment requests
- Validate request payloads
- Return API responses
- Delegate orchestration logic to service layer

Main APIs:
- Create Payment
- Fetch Payment By ID
- Fetch All Payments

---

### 2. Service Layer (Orchestration Engine)

The service layer acts as the core orchestration engine of the system.

Responsibilities:
- Coordinate payment execution flow
- Trigger routing logic
- Handle retries
- Execute failover strategy
- Persist payment state
- Enforce idempotency behavior

This layer contains the primary business logic of the application.

---

### 3. Routing Engine

The routing engine determines which provider should process the payment.

Current routing rules:

| Payment Method | Primary Provider |
|----------------|------------------|
|      CARD      |    Provider A    |
|      UPI       |    Provider B    |
The routing engine also determines failover providers if the primary provider fails.

---

### 4. Provider Connectors

Provider connectors simulate external payment service providers.

Implemented connectors:
- Provider A Connector
- Provider B Connector

Responsibilities:
- Simulate provider API calls
- Simulate provider failures
- Return provider responses

The connectors are intentionally abstracted behind interfaces to support future extensibility.

---

### 5. Retry and Failover Mechanism

If a provider request fails:
1. The system retries the same provider request for a fixed number of attempts.
2. If retries are exhausted, the orchestration engine switches to the failover provider.
3. Final payment status is persisted after orchestration completes.

This improves reliability and fault tolerance.

---

## Retry Strategy

Current retry configuration:
- Maximum retry attempts: 2

Example flow:
1. Provider A fails
2. Retry Provider A
3. Retry Provider A again
4. Switch to Provider B
5. Persist final result

---

## Idempotency Design

Idempotency prevents duplicate payment creation when clients retry requests.

Implementation approach:
- Clients send an `Idempotency-Key` header
- The key is stored along with the payment response
- Duplicate requests with the same key return the previously processed response

Current implementation uses database persistence for idempotency storage.

---

## Persistence Layer

PostgreSQL is used as the primary persistence layer.

Stored information includes:
- Payment details
- Payment status
- Provider information
- Retry count
- Failure history
- Idempotency key
- Processing timestamps

---

## Exception Handling

Global exception handling is implemented using `@RestControllerAdvice`.

Handled scenarios:
- Payment not found
- Invalid request payloads
- Generic internal server errors

The system returns structured error responses for API consumers.

---

## API Documentation

Swagger/OpenAPI documentation is integrated using SpringDoc.

Benefits:
- API discoverability
- Request/response visibility
- Easier manual testing
- Improved developer experience

---

## Containerization

The application is containerized using Docker.

Docker Compose is used to:
- Run Spring Boot application
- Run PostgreSQL database
- Simplify local setup

This improves portability and consistency across environments.

---

## Design Decisions

### Why Modular Monolith Instead of Microservices

Although payment orchestration systems are commonly distributed systems, this project was intentionally implemented as a modular monolith because:
- Assignment scope is limited
- Simpler local development
- Easier debugging and testing
- Reduced infrastructure complexity

However, provider connectors and orchestration logic are structured in a way that supports future migration into independent microservices if required.

---

## Future Improvements

Possible future enhancements:
- Redis-based idempotency store
- Circuit breaker pattern
- Async event-driven processing
- Real PSP integrations
- Metrics and observability
- Authentication and authorization
- Dynamic routing configuration
- Kubernetes deployment