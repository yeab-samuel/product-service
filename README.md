# Product Service — Lab 2

![CI](https://github.com/yeab-samuel/product-service/actions/workflows/ci.yml/badge.svg)

## Overview
A RESTful Product Catalogue API built with Spring Boot 3, featuring full CRUD operations, input validation, global exception handling, and auto-generated Swagger documentation.

## Setup & Run
```bash
mvn spring-boot:run
```
Then open: http://localhost:8080/swagger-ui.html

## API Endpoints

| Method | Path | Status | Description |
|--------|------|--------|-------------|
| GET | /api/v1/products | 200 OK | List all products |
| GET | /api/v1/products/{id} | 200 / 404 | Get product by ID |
| POST | /api/v1/products | 201 Created | Create a new product |
| PUT | /api/v1/products/{id} | 200 / 404 | Update a product |
| DELETE | /api/v1/products/{id} | 204 / 404 | Delete a product |

## Swagger UI
http://localhost:8080/swagger-ui.html

## Run Tests
```bash
mvn test
```
Expected: Tests run: 10, Failures: 0