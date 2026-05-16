# Testing Strategy

## Overview

The project includes automated unit tests covering the core orchestration flow, routing logic, controller behavior, and exception handling.

Testing was implemented to validate:
- Business logic correctness
- Routing behavior
- API validation
- Error handling
- Idempotency behavior
- Retry and failover functionality

The focus was on ensuring predictable orchestration behavior under both successful and failure scenarios.

---

# Testing Scope

The following layers are covered:

| Layer              |   Coverage                           |
|--------------------|--------------------------------------|
| Controller Layer   | Request validation and API responses |
| Service Layer      | Payment orchestration logic          |
| Routing Engine     | Provider selection logic             |
| Exception Handling | Structured error responses           |

---

# Testing Frameworks

| Tool             | Purpose                |
|------------------|------------------------|
| JUnit 5          | Unit testing           |
| Mockito          | Mocking dependencies   |
| Spring Boot Test | Spring testing support |
| MockMvc          | Controller/API testing |

---

# Test Categories

## 1. Sanity Tests

Sanity tests validate that the core functionality works as expected.

Covered scenarios:
- Create payment successfully
- Fetch payment successfully
- Correct provider routing
- Successful provider response handling

---

## 2. Regression Tests

Regression tests ensure new changes do not break existing behavior.

Covered scenarios:
- Retry mechanism behavior
- Failover logic
- Idempotency handling
- Global exception handling
- Payment persistence flow

---

## 3. Integration-Oriented Tests

These tests validate interaction between layers.

Covered scenarios:
- Controller to service interaction
- Exception propagation
- Request validation flow

For assignment simplicity, provider integrations are simulated internally rather than calling external services.

---

# Test Scenarios

## Positive Test Cases

| Scenario                     | Expected Result                           |
|------------------------------|-------------------------------------------|
| Create CARD payment          | Routed to Provider A                      |
| Create UPI payment           | Routed to Provider B                      |
| Successful provider response | Payment status SUCCESS                    |
| Fetch payment by ID          | Returns payment details                   |
| Fetch all payments           | Returns sorted payment list               |
| Retry succeeds               | Payment marked SUCCESS                    |
| Failover succeeds            | Payment completed using secondary provider|
| Duplicate idempotency key    | Existing response returned                |

---

# Negative Test Cases

| Scenario                              | Expected Result          |
|---------------------------------------|--------------------------|
| Invalid payment ID                    | 404 Not Found            |
| Negative payment amount               | 400 Bad Request          |
| Missing payment method                | 400 Bad Request          |
| Unsupported payment method            | Validation failure       |
| Provider failure after retries        | Payment marked FAILED    |
| Duplicate request without idempotency | Separate payment created |
| Internal server exception             | Structured 500 response  |

---

# Retry and Failover Testing

The orchestration engine was tested for:
- Retry attempt count
- Retry sequencing
- Failover switching behavior
- Final provider tracking
- Failure history persistence

Example flow tested:
1. Primary provider fails
2. Retry attempts executed
3. Failover provider selected
4. Final payment status persisted

---

# Idempotency Testing

Idempotency behavior was tested using repeated requests with identical:

Idempotency-Key

Expected behavior:
- Only one payment record created
- Previously processed response returned

---

# Exception Handling Testing

Global exception handling was validated for:
- Payment not found exceptions
- Validation exceptions
- Internal server errors

Tests verify:
- HTTP status codes
- Error message structure
- Consistent API error format

---

# Manual Testing

In addition to automated tests, APIs were manually validated using Postman.

Manually verified:
- Request payload validation
- Retry behavior
- Failover flow
- Swagger documentation
- Idempotency behavior
- Dockerized setup

---

# Test Execution

Run all tests:
mvn test

---

# Current Testing Limitations

The following were intentionally simplified for assignment scope:
- No external provider sandbox integrations
- No load or stress testing
- No distributed system testing
- No asynchronous processing tests

---

# Future Improvements

Potential testing enhancements:
- Integration testing with Testcontainers
- Contract testing for provider connectors
- Performance and load testing
- Chaos testing for failover scenarios
- End-to-end orchestration testing