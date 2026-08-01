# ShopSphere Product Service

## Overview

Product Service is a standalone microservice responsible for managing products in the ShopSphere e-commerce platform.

This service is developed using Java 21 and Spring Boot following production-grade backend engineering practices.

---

## Tech Stack

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL 17
- Maven
- Docker
- Spring Boot Actuator

---

## Responsibilities

Current Responsibilities

- Product Management
- Product CRUD APIs
- Product Validation
- Product Persistence

Future Responsibilities

- Redis Caching
- Product Search
- Product Events using RabbitMQ
- API Documentation
- Monitoring & Metrics

---

## Project Structure

```text
src/main/java/com/shopsphere/product

├── config
├── common
├── exception
│
└── product
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    └── mapper
```

---

## Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop
- PostgreSQL 17 Docker Image

---

## Run PostgreSQL

```bash
docker run -d \
  --name shopsphere-postgres \
  -e POSTGRES_DB=product_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:17
```

---

## Application Configuration

Configure the following environment variables.

| Variable | Value                                       |
|----------|---------------------------------------------|
| DB_URL | jdbc:postgresql://localhost:5432/product_db |
| DB_USERNAME | your_username                               |
| DB_PASSWORD | your_password                               |

Example

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Run Application

```bash
mvn clean spring-boot:run
```

or

Run **ProductServiceApplication** from IntelliJ IDEA.

---

## Health Check

Spring Boot Actuator

```
GET /actuator/health
```

Example

```json
{
  "status": "UP"
}
```

---

## Git Workflow

Main Branches

```
main
develop
```

Feature Branch Naming

```
feature/SPS-001-bootstrap-product-service
```

---

## Commit Convention

Examples

```
chore: bootstrap product service

feat(product): add create product api

fix(product): resolve validation issue

refactor(product): improve service layer
```

---

## License

This project is developed for learning production-grade backend engineering, Spring Boot microservices, distributed system design and industry-standard software development practices.