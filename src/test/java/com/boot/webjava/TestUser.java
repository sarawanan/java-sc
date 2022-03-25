package com.boot.webjava;

import com.boot.webjava.entity.User;
import com.boot.webjava.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestUser {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository repository;

    @Test
    public void testUserCreateAndFetch() {
        User sara = entityManager.persist(User
                .builder()
                .username("Sara")
                .password("password")
                .build());
        repository.findById(sara.getId()).ifPresent(user -> assertEquals(user.getId(), sara.getId()));
    }
}
