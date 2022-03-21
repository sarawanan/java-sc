package com.boot.webjava.dto;

import com.boot.webjava.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private int cartId;
    private List<CartItem> cartItems;
    private double totalPrice;
}
