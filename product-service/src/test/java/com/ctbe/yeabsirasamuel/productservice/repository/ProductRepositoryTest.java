package com.ctbe.yeabsirasamuel.productservice.repository;

import com.ctbe.yeabsirasamuel.productservice.model.Category;
import com.ctbe.yeabsirasamuel.productservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("dev")
class ProductRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired ProductRepository productRepo;

    private Category electronics;

    @BeforeEach
    void setUp() {
        electronics = em.persist(Category.builder().name("TestCategory").build());
        em.persist(Product.builder()
                .name("Laptop").price(new BigDecimal("999.00"))
                .stock(10).slug("laptop").category(electronics).build());
        em.persist(Product.builder()
                .name("Keyboard").price(new BigDecimal("49.99"))
                .stock(25).slug("keyboard").category(electronics).build());
        em.flush();
    }

    @Test
    void findAllWithCategory_returnsPageWithCategoryEagerlyLoaded() {
        Page<Product> page = productRepo.findAllWithCategory(Pageable.ofSize(10));
        assertThat(page.getTotalElements()).isEqualTo(2);
        page.getContent().forEach(p ->
                assertThat(p.getCategory().getName()).isEqualTo("TestCategory"));
    }

    @Test
    void searchByKeyword_matchesNameCaseInsensitive() {
        List<Product> results = productRepo.searchByKeyword("lap");
        assertThat(results).hasSize(1)
                .extracting(Product::getName).contains("Laptop");
    }

    @Test
    void delete_softDeletesProduct_doesNotAppearInSubsequentQueries() {
        Product laptop = productRepo.findBySlug("laptop").orElseThrow();
        productRepo.delete(laptop);
        em.flush();
        em.clear();
        assertThat(productRepo.findBySlug("laptop")).isEmpty();
        assertThat(productRepo.count()).isEqualTo(1);
    }
}
