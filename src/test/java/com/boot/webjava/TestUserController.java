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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void testUserCreate() throws Exception {
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(User
                        .builder()
                        .username("admin")
                        .id(1)
                        .password("admin")
                        .build());
        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                User.builder().username("admin").password("admin").build())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    @WithMockUser("admin")
    public void testAllUsers() throws Exception {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(
                        User.builder().id(1).username("admin").password("admin").build(),
                        User.builder().id(2).username("admin1").password("admin1").build()
                ));
        mockMvc.perform(get("/api/user"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].username").value("admin"))
                .andExpect(jsonPath("$.[1].username").value("admin1"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }
}
