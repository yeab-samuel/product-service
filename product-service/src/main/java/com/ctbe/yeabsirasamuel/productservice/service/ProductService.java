package com.ctbe.yeabsirasamuel.productservice.service;

import com.ctbe.yeabsirasamuel.productservice.dto.ProductRequest;
import com.ctbe.yeabsirasamuel.productservice.dto.ProductResponse;
import com.ctbe.yeabsirasamuel.productservice.exception.ResourceNotFoundException;
import com.ctbe.yeabsirasamuel.productservice.model.Product;
import com.ctbe.yeabsirasamuel.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // ── Read ─────────────────────────────────────────────────
    public List<ProductResponse> findAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public ProductResponse findById(Long id) {
        return toResponse(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    // ── Create ───────────────────────────────────────────────
    public ProductResponse create(ProductRequest req) {
        return toResponse(repo.save(toEntity(req)));
    }

    // ── Update ───────────────────────────────────────────────
    public ProductResponse update(Long id, ProductRequest req) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        existing.setName(req.getName());
        existing.setPrice(BigDecimal.valueOf(req.getPrice()));
        existing.setStock(req.getStockQty());
        // category stays as-is in old endpoint — use CatalogueService to change category
        return toResponse(repo.save(existing));
    }

    // ── Delete ───────────────────────────────────────────────
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException(id);
        repo.deleteById(id);
    }

    // ── Mapping helpers ───────────────────────────────────────
    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice() != null ? p.getPrice().doubleValue() : 0.0,
                p.getStock() != null ? p.getStock() : 0,
                p.getCategory() != null ? p.getCategory().getName() : null
        );
    }

    private Product toEntity(ProductRequest req) {
        return Product.builder()
                .name(req.getName())
                .price(BigDecimal.valueOf(req.getPrice()))
                .stock(req.getStockQty())
                .slug(req.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-"))
                .build();
    }
}
