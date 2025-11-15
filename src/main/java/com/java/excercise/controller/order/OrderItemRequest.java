package com.java.excercise.controller.order;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String productId;
    private int quantity;
}
