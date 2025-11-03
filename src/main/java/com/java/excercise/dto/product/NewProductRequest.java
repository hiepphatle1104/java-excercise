package com.java.excercise.dto.product;

import java.util.List;

public record NewProductRequest(
    String category,
    String name,
    String brand,
    String date,
    String status,
    String description,
    Double price, //them price
    List<String> images,
    ProductDetailRequest details
) {
}
