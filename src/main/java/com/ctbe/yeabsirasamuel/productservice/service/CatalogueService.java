package com.ctbe.yeabsirasamuel.productservice.service;

import com.ctbe.yeabsirasamuel.productservice.dto.request.CreateProductRequest;
import com.ctbe.yeabsirasamuel.productservice.dto.response.ProductDTO;
import com.ctbe.yeabsirasamuel.productservice.exception.ResourceNotFoundException;
import com.ctbe.yeabsirasamuel.productservice.mapper.ProductMapper;
import com.ctbe.yeabsirasamuel.productservice.model.Category;
import com.ctbe.yeabsirasamuel.productservice.model.Product;
import com.ctbe.yeabsirasamuel.productservice.repository.CategoryRepository;
import com.ctbe.yeabsirasamuel.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatalogueService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper mapper;

    public ProductDTO create(CreateProductRequest req) {
        Category cat = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + req.categoryId()));

        Product p = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stock(req.stock())
                .slug(slugify(req.name()))
                .category(cat)
                .build();

        return mapper.toDTO(productRepo.save(p));
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepo.findAllWithCategory(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public ProductDTO findBySlug(String slug) {
        return productRepo.findBySlug(slug)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + slug));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> search(String keyword, BigDecimal maxPrice) {
        List<Product> results = (keyword != null && !keyword.isBlank())
                ? productRepo.searchByKeyword(keyword)
                : productRepo.findAll();
        return results.stream()
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .map(mapper::toDTO)
                .toList();
    }

    public void delete(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        productRepo.delete(p);
        log.info("Soft-deleted product id={} name={}", p.getId(), p.getName());
    }

    private String slugify(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
