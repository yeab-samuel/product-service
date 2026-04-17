# Reflection — Lab 2

**1. Why should ProductRequest carry @Valid annotations instead of the Product entity?**
The DTO is the entry point for client data, so validation belongs there. The entity's job is to represent the database model, not validate API input. Keeping validation on the DTO means each class has one responsibility, and we avoid accidentally triggering validation logic inside the service layer.

**2. What is the purpose of the Location header on a POST 201 Created response?**
The Location header tells the client exactly where to find the newly created resource, for example `/api/v1/products/4`. This is mandated by RFC 7231 (HTTP/1.1 Semantics), which states that a 201 response SHOULD include a Location header pointing to the new resource.

**3. Difference between @ControllerAdvice and @ExceptionHandler?**
@ExceptionHandler handles exceptions within a single controller only. @ControllerAdvice is a global interceptor that applies exception handling across all controllers in the application. You use @ExceptionHandler for controller-specific errors and @ControllerAdvice when you want consistent error handling everywhere.

**4. What happens if @Transactional is removed from the test class?**
Database changes made in one test would persist into the next test. For example, a product created in the POST test would still exist when the GET test runs, causing unpredictable results and flaky tests. @Transactional rolls back all changes after each test method, keeping tests fully isolated.

**5. What does RFC 9457 define and why is it better?**
RFC 9457 defines a standard JSON format for HTTP error responses called Problem Details, with fields like type, title, status, and detail. It is better than a generic error message because clients can reliably parse and understand any error from any API that follows it, rather than each API inventing its own error format.

**6. Difference between integration tests (MockMvc) and unit tests (Mockito)?**
Unit tests (Mockito) test a single class in isolation by mocking all dependencies — they are fast and pinpoint exactly where a bug is. Integration tests (MockMvc) load the full Spring context and test how all layers work together — they are slower but catch wiring issues, validation errors, and routing problems that unit tests would miss.