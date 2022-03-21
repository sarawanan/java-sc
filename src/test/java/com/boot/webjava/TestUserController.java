package com.boot.webjava;

import com.boot.webjava.entity.User;
import com.boot.webjava.repository.CartItemRepository;
import com.boot.webjava.repository.CartRepository;
import com.boot.webjava.repository.ProductRepository;
import com.boot.webjava.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TestUserController {
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartItemRepository cartItemRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserCreate() throws Exception {
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(User
                        .builder()
                        .name("Saro")
                        .id(1)
                        .address("Test")
                        .build());
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                User.builder().name("Saro").address("Test").build())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    public void testAllUsers() throws Exception {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(
                        User.builder().id(1).name("Saro").address("Test").build(),
                        User.builder().id(2).name("Saro1").address("Test1").build()
                ));
        mockMvc.perform(get("/api/user"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Saro"))
                .andExpect(jsonPath("$.[1].name").value("Saro1"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }
}
