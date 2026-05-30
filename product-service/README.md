# Product Service — Lab 3

[![Java CI](https://github.com/yeab-samuel/product-service/actions/workflows/ci.yml/badge.svg?branch=lab3)](https://github.com/yeab-samuel/product-service/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A Spring Boot RESTful product catalogue API built as part of SECT-4221 Enterprise Application Development. Lab 3 migrates the Lab 2 H2 in-memory database to PostgreSQL using Flyway schema migrations, introduces a `Category` entity with JPA relationships, adds pagination and soft delete, and separates dev/prod Spring Profiles.

---

## Features

- **PostgreSQL** database in production via Docker Compose
- **Flyway** versioned schema migrations (V1–V5)
- **Category entity** with `@OneToMany` / `@ManyToOne` JPA relationship to `Product`
- **Soft delete** — products are flagged `deleted = true` rather than physically removed
- **Pagination** on catalogue endpoints using Spring Data `Pageable`
- **Product slug** — URL-friendly identifier auto-derived from product name
- **Case-insensitive keyword search** across name and description
- **Spring Profiles** — `dev` (H2 in-memory) and `prod` (PostgreSQL)
- **Actuator** endpoints including `/actuator/flyway` for migration visibility
- **OpenAPI / Swagger UI** at `/swagger-ui.html`
- **17 passing tests** across controller, service, and repository layers

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Database (prod) | PostgreSQL 15 |
| Database (dev/test) | H2 in-memory |
| Migrations | Flyway 10 |
| ORM | Hibernate / Spring Data JPA |
| Build | Maven 3 |
| Containerisation | Docker Compose |
| Docs | SpringDoc OpenAPI 3 |
| Testing | JUnit 5, Mockito, MockMvc |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker Desktop (for the prod profile)

### Run in dev mode (H2)

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` with an H2 in-memory database. Flyway migrations run automatically.

### Run in prod mode (PostgreSQL)

```bash
# 1. Start the database container
docker compose up db -d

# 2. Run the app with the prod profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Environment variables used by the prod profile:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/shopwave` | JDBC URL |
| `DB_USER` | `shopwave` | Database username |
| `DB_PASS` | `shopwave` | Database password |

---

## API Endpoints

### Products (legacy v1)

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/products` | List all products |
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products/{id}` | Get product by ID |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Soft-delete a product |

### Catalogue (v1 — paginated)

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/catalogue` | Paginated product list (with category) |
| `GET` | `/api/v1/catalogue/search?q=keyword` | Case-insensitive search |
| `GET` | `/api/v1/catalogue/{slug}` | Get product by slug |

### Categories

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/categories` | List all categories |
| `POST` | `/api/v1/categories` | Create a category |
| `DELETE` | `/api/v1/categories/{id}` | Delete a category |

Full interactive docs: **`http://localhost:8080/swagger-ui.html`**

---

## Database Migrations

| Version | Description |
|---|---|
| V1 | Create `categories` table |
| V2 | Create `products` table with FK to categories |
| V3 | Add `deleted` column for soft delete |
| V4 | Add `slug` column (zero-downtime, 3-step) |
| V5 | Seed 5 default categories |

---

## Running Tests

```bash
mvn clean test
```

```
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

Test coverage includes:

- `CatalogueControllerTest` — paginated catalogue endpoints (4 tests)
- `ProductControllerTest` — CRUD product endpoints (9 tests)
- `ProductServiceApplicationTests` — context loads (1 test)
- `ProductRepositoryTest` — soft delete, search, eager category loading (3 tests)

---

## Project Structure

```
src/
├── main/
│   ├── java/com/ctbe/yeabsirasamuel/productservice/
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Request / response records
│   │   ├── exception/          # Global error handling
│   │   ├── mapper/             # Entity ↔ DTO mapping
│   │   ├── model/              # JPA entities (Product, Category)
│   │   ├── repository/         # Spring Data repositories
│   │   └── service/            # Business logic
│   └── resources/
│       ├── db/migration/       # Flyway SQL migrations V1–V5
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/...                # JUnit 5 tests
```

---

## CI/CD

GitHub Actions runs `mvn test` on every push to `lab3`. See [`.github/workflows/ci.yml`](.github/workflows/ci.yml).

---

## Author

**Yeabsira Samuel** — SECT-4221 Enterprise Application Development, Lab 3