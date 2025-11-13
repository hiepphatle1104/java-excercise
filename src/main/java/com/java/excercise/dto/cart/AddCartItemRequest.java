package com.java.excercise.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartItemRequest {
    private String productId;
    private Integer quantity;
}
