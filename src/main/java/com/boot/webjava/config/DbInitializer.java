package com.boot.webjava.config;

import com.boot.webjava.entity.Product;
import com.boot.webjava.entity.User;
import com.boot.webjava.repository.ProductRepository;
import com.boot.webjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.save(User.builder().name("Sara").address("Test Address").build());
        productRepository.save(Product.builder().name("Rice").qty(10).price(100).build());
    }
}
