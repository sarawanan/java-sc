package com.boot.webjava;

import com.boot.webjava.entity.Product;
import com.boot.webjava.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestProduct {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepo;

    @Test
    public void testCreateAndFetchProduct() {
        Product rice = entityManager.persist(Product
                .builder()
                .name("Rice 1KG")
                .price(50)
                .qty(100)
                .build());
        productRepo.findById(rice.getId()).ifPresent(product -> assertEquals(product.getId(), rice.getId()));
    }
}
