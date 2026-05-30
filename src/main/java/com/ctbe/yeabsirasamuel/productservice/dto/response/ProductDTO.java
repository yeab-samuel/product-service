package com.ctbe.yeabsirasamuel.productservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String slug,
        String categoryName,
        Long categoryId,
        LocalDateTime createdAt
) {}
