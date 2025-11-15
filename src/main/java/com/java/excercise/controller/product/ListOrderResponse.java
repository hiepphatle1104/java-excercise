package com.java.excercise.controller.product;

import com.java.excercise.model.entities.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListOrderResponse {
    private Order order;
    private String userId;
}
