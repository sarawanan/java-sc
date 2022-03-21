package com.boot.webjava.controller;

import com.boot.webjava.entity.Product;
import com.boot.webjava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductRepository productRepo;

    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @GetMapping("/product")
    public List<Product> getProducts() {
        return productRepo.findAll();
    }
}
