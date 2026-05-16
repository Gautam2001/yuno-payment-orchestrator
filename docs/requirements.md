# Requirements

## Functional Requirements

### 1. Create Payment API

The system should expose an API to create a payment request.

The API should:
- Accept payment details from the client
- Route the payment to the appropriate provider
- Persist payment information
- Return payment status and provider details

---

### 2. Fetch Payment API

The system should expose an API to fetch payment details using payment ID.

The API should:
- Return stored payment information
- Return payment status
- Return provider details
- Handle invalid or missing payment IDs gracefully

---

### 3. Fetch All Payments API

The system should expose an API to retrieve all processed payments.

The API should:
- Return both successful and failed payments
- Return payments ordered by latest entries first
- Help with operational visibility and debugging

---

### 4. Payment Routing

The orchestration engine should route payments based on payment method.
- CARD payments are routed to Provider A
- UPI payments are routed to Provider B

---

### 5. Retry Mechanism

If a provider request fails, the system should retry the payment request before marking it as failed.

Retry behavior:
- Fixed retry attempts
- Retry against the same provider before failover

---

### 6. Failover Support

If retries are exhausted for the primary provider, the orchestration engine should switch to a failover provider.

This improves payment reliability and fault tolerance.

---

### 7. Idempotency

The system should support idempotent payment creation.

Behavior:
- Duplicate requests with the same idempotency key should not create duplicate payments
- Previously processed responses should be returned

---

### 8. Payment Status Tracking

The system should track payment lifecycle states.

Supported statuses:
- SUCCESS
- FAILED

Additional metadata:
- Retry count
- Provider used
- Failure history

---

### 9. Exception Handling

The system should provide structured error responses for:
- Invalid requests
- Missing resources
- Internal server errors

---

### 10. API Documentation

The system should expose Swagger/OpenAPI documentation for easier API exploration and testing.

---

## Non-Functional Requirements

### 1. Reliability

The system should support retry and failover mechanisms to improve payment success rate.

---

### 2. Maintainability

The project should follow clean layered architecture principles to keep the codebase modular and maintainable.

---

### 3. Extensibility

The provider connector design should support future onboarding of additional payment providers with minimal code changes.

---

### 4. Observability

The system should include logging to improve debugging and operational visibility.

---

### 5. Fault Tolerance

Provider failures should not immediately fail the payment flow.

The orchestration engine should:
- Retry failed requests
- Switch to fallback providers when needed

---

### 6. API Usability

The APIs should:
- Return structured responses
- Use meaningful HTTP status codes
- Provide clear validation messages

---

### 7. Containerization

The application should support Docker-based execution for simplified setup and deployment.

---

### 8. Documentation Quality

The repository should contain:
- Setup instructions
- API usage documentation
- Testing documentation
- Architecture explanation

---

### 9. Testability

The project should support automated testing for:
- Service layer
- Routing logic
- Controller layer
- Exception handling

---

### 10. Performance Considerations

The system should:
- Track payment processing duration
- Avoid duplicate payment processing
- Maintain predictable retry behavior
- Keep orchestration flow lightweight for assignment scope