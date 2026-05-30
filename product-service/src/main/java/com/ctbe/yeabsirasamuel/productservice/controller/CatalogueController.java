package com.ctbe.yeabsirasamuel.productservice.controller;

import com.ctbe.yeabsirasamuel.productservice.dto.request.CreateProductRequest;
import com.ctbe.yeabsirasamuel.productservice.dto.response.ProductDTO;
import com.ctbe.yeabsirasamuel.productservice.service.CatalogueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogue")
@Tag(name = "Catalogue", description = "Paginated product catalogue — Lab 3")
public class CatalogueController {

    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    // GET /api/v1/catalogue?page=0&size=10&sort=price,asc
    @GetMapping
    @Operation(summary = "List all products (paginated)")
    public ResponseEntity<Page<ProductDTO>> list(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(catalogueService.findAll(pageable));
    }

    // GET /api/v1/catalogue/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogueService.findById(id));
    }

    // GET /api/v1/catalogue/slug/{slug}
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDTO> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(catalogueService.findBySlug(slug));
    }

    // GET /api/v1/catalogue/search?keyword=java&maxPrice=50.00
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(catalogueService.search(keyword, maxPrice));
    }

    // POST /api/v1/catalogue
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody CreateProductRequest req) {
        return catalogueService.create(req);
    }

    // DELETE /api/v1/catalogue/{id} — soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        catalogueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
