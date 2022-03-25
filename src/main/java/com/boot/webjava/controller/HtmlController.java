package com.boot.webjava.controller;

import com.boot.webjava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HtmlController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/")
    public String getCart(Principal user, Model model) {
        model.addAttribute("username", user.getName());
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }
}
