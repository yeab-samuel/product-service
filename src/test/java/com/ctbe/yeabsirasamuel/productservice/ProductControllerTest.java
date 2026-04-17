package com.ctbe.yeabsirasamuel.productservice;

import com.ctbe.yeabsirasamuel.productservice.model.Product;
import com.ctbe.yeabsirasamuel.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductRepository repo;
    @Autowired ObjectMapper mapper;

    private Long savedId;

    @BeforeEach
    void setUp() {
        Product p = repo.save(new Product("Test Laptop", 999.0, 10, "Electronics"));
        savedId = p.getId();
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void getById_returns200_whenExists() throws Exception {
        mockMvc.perform(get("/api/v1/products/" + savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Laptop")))
                .andExpect(jsonPath("$.category", is("Electronics")));
    }

    @Test
    void create_returns201_withLocation() throws Exception {
        String body = """
                {"name":"Headphones","price":89.99,"stockQty":50,"category":"Audio"}
                """;
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Headphones")));
    }

    @Test
    void update_returns200() throws Exception {
        String body = """
                {"name":"Pro Laptop","price":1299.0,"stockQty":5,"category":"Electronics"}
                """;
        mockMvc.perform(put("/api/v1/products/" + savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Pro Laptop")))
                .andExpect(jsonPath("$.price", is(1299.0)));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/products/" + savedId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", containsString("9999")));
    }

    @Test
    void delete_returns404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/products/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returns400_whenNameBlank() throws Exception {
        String invalid = """
                {"name":"","price":10.0,"stockQty":1,"category":"Tech"}
                """;
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("Name is required")));
    }

    @Test
    void create_returns400_whenPriceInvalid() throws Exception {
        String invalid = """
                {"name":"Widget","price":-1,"stockQty":1,"category":"Tech"}
                """;
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest());
    }
}