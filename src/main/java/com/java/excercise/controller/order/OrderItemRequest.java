package com.java.excercise.controller.order;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String id;
    private int quantity;
}
