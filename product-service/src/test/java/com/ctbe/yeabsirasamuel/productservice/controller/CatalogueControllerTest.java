package com.ctbe.yeabsirasamuel.productservice.controller;

import com.ctbe.yeabsirasamuel.productservice.dto.response.ProductDTO;
import com.ctbe.yeabsirasamuel.productservice.exception.ResourceNotFoundException;
import com.ctbe.yeabsirasamuel.productservice.service.CatalogueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogueController.class)
@ActiveProfiles("dev")
class CatalogueControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean CatalogueService catalogueService;

    @Test
    void getProducts_returnsPaginatedResponse() throws Exception {
        ProductDTO dto = new ProductDTO(1L, "Laptop", null,
                new BigDecimal("999.00"), 10, "laptop", "Electronics", 1L, null);
        Page<ProductDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(catalogueService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/catalogue")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.content[0].categoryName").value("Electronics"));
    }

    @Test
    void getProductById_whenNotFound_returns404() throws Exception {
        when(catalogueService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Product not found: 999"));

        mockMvc.perform(get("/api/v1/catalogue/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_withValidBody_returns201() throws Exception {
        ProductDTO dto = new ProductDTO(2L, "Keyboard", null,
                new BigDecimal("49.99"), 20, "keyboard", "Electronics", 1L, null);
        when(catalogueService.create(any())).thenReturn(dto);

        String body = """
                {"name":"Keyboard","price":49.99,"stock":20,"categoryId":1}
                """;

        mockMvc.perform(post("/api/v1/catalogue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.slug").value("keyboard"));
    }

    @Test
    void softDelete_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/catalogue/1"))
                .andExpect(status().isNoContent());
        verify(catalogueService, times(1)).delete(1L);
    }
}
