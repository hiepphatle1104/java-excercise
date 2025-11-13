package com.java.excercise.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {
    private List<CartItemDTO> cartItem;
    private double total;
}
