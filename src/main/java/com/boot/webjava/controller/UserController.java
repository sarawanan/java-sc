package com.boot.webjava.controller;

import com.boot.webjava.entity.User;
import com.boot.webjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
