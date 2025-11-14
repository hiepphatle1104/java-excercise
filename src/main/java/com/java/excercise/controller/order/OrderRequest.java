package com.java.excercise.controller.order;

import com.java.excercise.model.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private PaymentMethod method;
    private List<OrderItemRequest> items;
}
