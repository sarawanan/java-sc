package com.boot.webjava.config;

import com.boot.webjava.entity.Product;
import com.boot.webjava.entity.User;
import com.boot.webjava.repository.ProductRepository;
import com.boot.webjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DbInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(User.builder().username("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .role("ADMIN").build());
        productRepository.save(Product.builder().name("Rice").qty(10).price(80).build());
        productRepository.save(Product.builder().name("Wheat").qty(10).price(70).build());
        productRepository.save(Product.builder().name("Sugar").qty(40).price(50).build());
        productRepository.save(Product.builder().name("Bread").qty(30).price(20).build());
        productRepository.save(Product.builder().name("Butter").qty(10).price(10).build());
    }
}
