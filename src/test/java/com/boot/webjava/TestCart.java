package com.boot.webjava;

import com.boot.webjava.entity.Cart;
import com.boot.webjava.entity.CartItem;
import com.boot.webjava.entity.Product;
import com.boot.webjava.entity.User;
import com.boot.webjava.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestCart {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CartRepository repository;

    @Test
    public void testCartCreate() {
        User sara = entityManager.persist(User
                .builder()
                .name("Sara")
                .address("test address")
                .build());
        Product rice = entityManager.persist(Product
                .builder()
                .name("Rice 1KG")
                .price(50)
                .qty(100)
                .build());
        Product wheat = entityManager.persist(Product
                .builder()
                .name("Wheat 1KG")
                .price(40)
                .qty(50)
                .build());
        Cart cartSara = entityManager.persist(Cart
                .builder()
                .user(sara)
                .list(List.of(
                        CartItem.builder().product(rice).qty(10).price(500).build(),
                        CartItem.builder().product(wheat).qty(5).price(200).build()))
                .totalPrice(700)
                .build());

        repository.findById(cartSara.getId()).ifPresent(cart -> assertEquals(cart.getList().size(), 2));
    }
}
