package com.java.excercise.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String id; // ID của CartItem
    private String productId;
    private String productName;
    private double price;
    private String url;
    private Integer quantity;
}
