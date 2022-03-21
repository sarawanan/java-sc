package com.boot.webjava.controller;

import com.boot.webjava.dto.RequestDto;
import com.boot.webjava.entity.Cart;
import com.boot.webjava.entity.CartItem;
import com.boot.webjava.exception.CartNotFoundException;
import com.boot.webjava.exception.CustomerNotFoundException;
import com.boot.webjava.exception.ProductNotFoundException;
import com.boot.webjava.repository.CartItemRepository;
import com.boot.webjava.repository.CartRepository;
import com.boot.webjava.repository.ProductRepository;
import com.boot.webjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/cart/{userId}")
    public Cart createCart(@PathVariable int userId) {
        return userRepository.findById(userId)
                .map(user -> cartRepository.save(Cart.builder().user(user).build()))
                .orElseThrow(CustomerNotFoundException::new);
    }

    @GetMapping("/cart/{userId}")
    public Cart getCart(@PathVariable int userId) {
        return userRepository.findById(userId)
                .map(user -> cartRepository.findByUser(user)).orElseThrow(CustomerNotFoundException::new);
    }

    @PostMapping("/cart/{cardId}/add")
    public Cart addToCart(@PathVariable int cardId, @RequestBody RequestDto request) {
        return cartRepository.findById(cardId)
                .map(cart -> {
                    productRepository.findById(request.getProductId())
                            .map(p -> {
                                List<CartItem> cartItemList = cart.getList();
                                CartItem cartItem = cartItemRepository.save(CartItem
                                        .builder()
                                        .product(p)
                                        .qty(request.getQty())
                                        .price(p.getPrice() * request.getQty())
                                        .build());
                                cartItemList.add(cartItem);
                                cart.setTotalPrice(cart.getTotalPrice() + cartItem.getPrice());
                                return cartItem;
                            }).orElseThrow(ProductNotFoundException::new);
                    return cartRepository.save(cart);
                }).orElseThrow(CartNotFoundException::new);
    }
}
