package com.java.excercise.dto.product;

import java.util.List;

public record NewProductRequest(
    String category,
    String userId,
    String name,
    String brand,
    String date,
    String status,
    String description,
    List<String> images,
    ProductDetailRequest details
) {
}
