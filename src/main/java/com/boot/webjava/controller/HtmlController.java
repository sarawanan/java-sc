package com.boot.webjava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HtmlController {
    @GetMapping("/")
    public String getCart(Principal user, Model model) {
        model.addAttribute("username", user.getName());
        return "index";
    }
}
