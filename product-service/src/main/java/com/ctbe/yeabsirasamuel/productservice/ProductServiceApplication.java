package com.ctbe.yeabsirasamuel.productservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Product Service API",
		version = "1.0.0",
		description = "RESTful Product Catalogue — Lab 2"
))
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	// Seed data removed — Flyway V5 handles category seeding.
	// Products are created via POST /api/v1/catalogue after startup.
}