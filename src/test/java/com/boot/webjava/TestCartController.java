package com.boot.webjava;

import com.boot.webjava.entity.Cart;
import com.boot.webjava.entity.User;
import com.boot.webjava.exception.CustomerNotFoundException;
import com.boot.webjava.repository.CartItemRepository;
import com.boot.webjava.repository.CartRepository;
import com.boot.webjava.repository.ProductRepository;
import com.boot.webjava.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TestCartController {
    @MockBean
    private CartItemRepository cartItemRepository;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateCartWithException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenThrow(new CustomerNotFoundException());
        mockMvc.perform(post("/api/cart/1"))
                .andExpect(status().is(404))
                .andExpect(exception ->
                        assertTrue(exception.getResolvedException() instanceof CustomerNotFoundException))
                .andExpect(exception ->
                        assertEquals(Objects.requireNonNull(exception.getResolvedException()).getMessage(),
                                "Customer Not Found!"));
    }

    @Test
    public void testCreateCart() throws Exception {
        User user = User.builder().id(1).name("Sara").address("Test Address").build();
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(
                Optional.of(user));
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(Cart.builder().id(1).user(user).build());
        mockMvc.perform(post("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.user.name").value("Sara"));
    }

    @Test
    public void testGetCartWithException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenThrow(new CustomerNotFoundException());
        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().is(404))
                .andExpect(exception ->
                        assertTrue(exception.getResolvedException() instanceof CustomerNotFoundException))
                .andExpect(exception ->
                        assertEquals(Objects.requireNonNull(exception.getResolvedException()).getMessage(),
                                "Customer Not Found!"));
    }

    @Test
    public void testGetCart() throws Exception {
        User user = User.builder().id(1).name("Sara").address("Test Address").build();
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Cart.builder().id(1).user(user).build());
        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.user.name").value("Sara"));
    }
}
