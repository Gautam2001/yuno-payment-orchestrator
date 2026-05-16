# API Contracts

## Base URL
http://localhost:8080/payments

---

# 1. Create Payment

## Endpoint
POST /payments

---

## Headers

| Header          |Required| Description                         |
|-----------------|--------|-------------------------------------|
| Content-Type    | Yes    | application/json                    |
| Idempotency-Key | Yes    | Prevents duplicate payment creation |

---

## Request Body

{
  "amount": 1500,
  "currency": "INR",
  "paymentMethod": "CARD"
}

---

## Request Parameters

| Field         | Type   |Required| Description    |
|---------------|--------|--------|----------------|
| amount        | Decimal| Yes    | Payment amount |
| currency      | String | Yes    | Currency code  |
| paymentMethod | String | Yes    | CARD or UPI    |

---

## Success Response

### HTTP 200 OK
{
  "paymentId": "c53ef4c8-39ab-4dd0-b2cf-4c6d9d48e4f5",
  "amount": 1500,
  "currency": "INR",
  "paymentMethod": "CARD",
  "paymentStatus": "SUCCESS",
  "finalProvider": "PROVIDER_A",
  "retryCount": 1,
  "attemptHistory": "SUCCESS -> PROVIDER_A on attempt 1"
}

---

## Failure Response

### HTTP 500 Internal Server Error

{
  "timestamp": "2026-05-15T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Payment processing failed",
  "path": "/payments"
}

---

## Validation Error Response

### HTTP 400 Bad Request

{
  "timestamp": "2026-05-15T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Amount must be greater than zero",
  "path": "/payments"
}

---

# 2. Fetch Payment By ID

## Endpoint

http
GET /payments/{paymentId}

---

## Path Parameter

| Parameter | Type | Description |
|-----------|------|-------------|
| paymentId | UUID | Unique payment identifier |

---

## Success Response

### HTTP 200 OK

{
  "paymentId": "c53ef4c8-39ab-4dd0-b2cf-4c6d9d48e4f5",
  "amount": 1500,
  "currency": "INR",
  "paymentMethod": "CARD",
  "paymentStatus": "SUCCESS",
  "finalProvider": "PROVIDER_A",
  "retryCount": 1,
  "attemptHistory": "SUCCESS -> PROVIDER_A on attempt 1"
}

---

## Not Found Response

### HTTP 404 Not Found

{
  "timestamp": "2026-05-15T12:00:00",
  "status": 404,
  "error": "Payment Not Found",
  "message": "Payment not found with id: c53ef4c8-39ab-4dd0-b2cf-4c6d9d48e4f5",
  "path": "/payments/c53ef4c8-39ab-4dd0-b2cf-4c6d9d48e4f5"
}

---

# 3. Fetch All Payments

## Endpoint

GET /payments

---

## Success Response

### HTTP 200 OK

[
  {
    "paymentId": "d2f4d52b-4314-42c8-b98f-cf6a2f7a1111",
    "amount": 2000,
    "currency": "INR",
    "paymentMethod": "UPI",
    "paymentStatus": "FAILED",
    "finalProvider": "PROVIDER_B",
    "retryCount": 3,
    "attemptHistory": "FAILED -> PROVIDER_B on attempt 1"
  },
  {
    "paymentId": "a3c8f91f-2190-4b1e-8224-d5c61f4a2222",
    "amount": 1000,
    "currency": "INR",
    "paymentMethod": "CARD",
    "paymentStatus": "SUCCESS",
    "finalProvider": "PROVIDER_A",
    "retryCount": 0,
    "attemptHistory": "SUCCESS -> PROVIDER_A on attempt 1"
  }
]

---

# Routing Behavior

Current provider routing configuration:

| Payment Method | Primary Provider | Failover Provider |
|----------------|------------------|-------------------|
| CARD           | Provider A       | Provider B        |
| UPI            | Provider B       | Provider A        |

---

# Retry and Failover Flow

## Example Scenario

1. Payment received
2. Primary provider selected
3. Provider request fails
4. Retry triggered
5. Retry limit exceeded
6. Failover provider selected
7. Final response persisted

---

# Idempotency Flow

## Behavior

If the client sends the same request multiple times using the same:

Idempotency-Key

the system:
- avoids duplicate payment creation
- returns the previously stored response

---

## Example Header

Idempotency-Key: payment-12345

---

# Provider Integration Design

Provider connectors are abstracted using interfaces.

Benefits:
- Easier extensibility
- Cleaner orchestration logic
- Future PSP onboarding support

Current providers are simulated internally for assignment scope.

---

# Swagger Documentation

Swagger UI:
http://localhost:8080/swagger-ui/index.html

OpenAPI specification:
http://localhost:8080/v3/api-docs
