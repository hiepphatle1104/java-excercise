package com.java.excercise.controller.order;

import com.java.excercise.model.enums.OrderStatus;
import com.java.excercise.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private double amount;
    private PaymentMethod method;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
