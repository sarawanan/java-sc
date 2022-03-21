package com.boot.webjava;

import com.boot.webjava.dto.RequestDto;
import com.boot.webjava.entity.Cart;
import com.boot.webjava.entity.CartItem;
import com.boot.webjava.entity.Product;
import com.boot.webjava.entity.User;
import com.boot.webjava.exception.CartNotFoundException;
import com.boot.webjava.exception.CustomerNotFoundException;
import com.boot.webjava.exception.ProductNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testAddToCartWithNoCartException() throws Exception {
        Mockito.when(cartRepository.findById(Mockito.anyInt())).thenThrow(new CartNotFoundException());
        String json = new ObjectMapper().writeValueAsString(RequestDto.builder().productId(1).qty(10).build());
        mockMvc.perform(post("/api/cart/1/add").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof CartNotFoundException))
                .andExpect(exception ->
                        assertEquals(Objects.requireNonNull(exception.getResolvedException()).getMessage(),
                                "Cart Not Found!"));
    }

    @Test
    public void testAddToCartWithNoProductException() throws Exception {
        User user = User.builder().id(1).name("Sara").address("Test Address").build();
        String json = new ObjectMapper().writeValueAsString(RequestDto.builder().productId(1).qty(10).build());
        Mockito.when(cartRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(Cart.builder().id(1).user(user).build()));
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenThrow(new ProductNotFoundException());
        mockMvc.perform(post("/api/cart/1/add").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof ProductNotFoundException))
                .andExpect(exception ->
                        assertEquals(Objects.requireNonNull(exception.getResolvedException()).getMessage(),
                                "Product Not Found!"));
    }

    @Test
    public void testAddToCart() throws Exception {
        User user = User.builder().id(1).name("Sara").address("Test Address").build();
        String json = new ObjectMapper().writeValueAsString(RequestDto.builder().productId(1).qty(10).build());
        Mockito.when(cartRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(Cart.builder().id(1).list(new ArrayList<>()).user(user).build()));
        Product rice = Product.builder().id(1).name("Rice").price(100).qty(10).build();
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(
                Optional.of(rice));
        Mockito.when(cartItemRepository.save(Mockito.any())).thenReturn(
                CartItem.builder().id(1).product(rice).qty(2).price(100).build());
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(
                Cart.builder().user(user).id(1)
                        .list(List.of(CartItem.builder().id(1).product(rice).qty(2).price(100).build()))
                        .totalPrice(100).build());
        mockMvc.perform(post("/api/cart/1/add").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}
