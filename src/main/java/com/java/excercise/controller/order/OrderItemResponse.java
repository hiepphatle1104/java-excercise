package com.java.excercise.controller.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private String productId;
    private int quantity;
}
