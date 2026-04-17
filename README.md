# product-service

![CI](https://github.com/yeab-samuel/product-service/actions/workflows/ci.yml/badge.svg)

A RESTful product microservice built with Spring Boot 3.

## Getting Started

```bash
mvn spring-boot:run
```

## Endpoints

| Method | Path           | Description          |
|--------|----------------|----------------------|
| GET    | /products      | List all products    |
| GET    | /products/{id} | Get product by ID    |
| POST   | /products      | Create a new product |
| GET    | /health        | Service health check |

## Reflection

**1. Purpose of the Service layer?**
The Service layer holds all business logic and acts as the bridge between Controller and Repository. A Controller should never call a Repository directly because that would mix HTTP handling with data access, making the code harder to test and maintain.

**2. Why layered architecture?**
It separates concerns so each class has one job. This makes the codebase easier to test, extend, and understand.

**3. Spring Data JPA advantages over plain JDBC?**
First, free CRUD methods like findAll() and save() with no SQL needed. Second, derived query methods like findByNameContainingIgnoreCase() are auto-generated from the method name alone.

**4. Why mock ProductRepository in tests?**
To isolate ProductService and test only its logic. A real repository would require a database, making tests slow and fragile.

**5. create-drop vs update?**
create-drop rebuilds the schema fresh every run — ideal for development and testing. update preserves existing data — used in production where data must not be lost.