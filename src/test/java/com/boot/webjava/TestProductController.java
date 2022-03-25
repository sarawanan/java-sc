package com.boot.webjava;

import com.boot.webjava.entity.Product;
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
public class TestProductController {
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
    @WithMockUser("admin")
    public void testCreateProduct() throws Exception {
        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(Product.builder().id(1).name("Rice").qty(10).price(100).build());
        mockMvc.perform(post("/api/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                Product
                                        .builder()
                                        .name("Rice")
                                        .qty(10)
                                        .price(100)
                                        .build())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rice"));
    }

    @Test
    @WithMockUser("admin")
    public void testAllProducts() throws Exception {
        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(
                        Product.builder().id(1).name("Rice").qty(10).price(100).build(),
                        Product.builder().id(2).name("Wheat").qty(15).price(150).build()));
        mockMvc.perform(get("/api/product"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Rice"))
                .andExpect(jsonPath("$.[1].name").value("Wheat"))
                .andExpect(jsonPath("$.[1].price").value(150));
    }
}
