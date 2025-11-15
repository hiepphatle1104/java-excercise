package com.java.excercise.controller.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String userId;
    private String productId;
    private String productImg;
    private String productName;
    private double price;
    private int quantity;
}
