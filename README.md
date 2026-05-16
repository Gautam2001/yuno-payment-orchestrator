# Payment Orchestrator

A simplified payment orchestration system built using Java and Spring Boot, inspired by real-world payment platforms like Yuno.

The system routes payments to different providers based on payment method, supports retry and failover mechanisms, handles idempotent requests, and tracks payment status throughout the orchestration lifecycle.

---

## Features

- Create Payment API
- Fetch Payment API
- Fetch All Payments API
- Payment Routing Engine
- Retry Mechanism
- Failover Between Providers
- Idempotency Support
- Payment Status Tracking
- Global Exception Handling
- Swagger/OpenAPI Documentation
- Automated Unit Testing
- Dockerized Setup

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- Swagger / OpenAPI

---

## High Level Architecture

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

## Payment Routing Logic

| Payment Method | Primary Provider |
|----------------|------------------|
|      CARD      |    Provider A    |
|      UPI       |    Provider B    |

If the primary provider fails, the orchestration engine retries the request before switching to the failover provider.

---

## APIs

|Method|     Endpoint   |     Description     |
|------|----------------|---------------------|
| POST | /payments      | Create payment      |
| GET  | /payments/{id} | Fetch payment by ID |
| GET  | /payments      | Fetch all payments  |

---

## Running the Application Locally

### 1. Clone Repository
git clone https://github.com/Gautam2001/yuno-payment-orchestrator

---

### 2. Configure PostgreSQL

Create a PostgreSQL database named:
yuno_payment

Update database credentials inside:
src/main/resources/application.properties

---

### 3. Run Application
mvn spring-boot:run

Application runs on:
http://localhost:8080


---

## Swagger API Documentation

Swagger UI:
http://localhost:8080/swagger-ui/index.html


OpenAPI Docs:
http://localhost:8080/v3/api-docs


---

## Running Tests
mvn test

---

## Docker Setup

### Build and Start Containers
docker compose up --build

This starts:
- Spring Boot application
- PostgreSQL database

---

## Idempotency Support

Create payment API supports idempotency using:
Idempotency-Key
in the header.

Sending the same request with the same key returns the previously processed response instead of creating duplicate payments.

---

## Additional Documentation

- [Architecture](docs/architecture.md)
- [Requirements](docs/requirements.md)
- [API Contracts](docs/api-contracts.md)
- [Testing Strategy](docs/testing-strategy.md)
- [Performance Considerations](docs/performance-considerations.md)
- [Prompts Used During Development](docs/prompts-used.md)

---

## Assumptions

- Provider connectors are mocked/simulated.
- Retry attempts are limited to maintain predictable processing behavior.
- Idempotency is implemented using database persistence.
- The project is designed as a modular monolith for simplicity and assignment scope.

---

## Future Improvements

- Redis-based idempotency store
- Real external provider integrations
- Circuit breaker support
- Metrics and monitoring dashboards
- Authentication and authorization
- Async event-driven processing