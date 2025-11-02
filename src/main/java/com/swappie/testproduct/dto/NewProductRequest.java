package com.swappie.testproduct.dto;

import java.util.List;

public record NewProductRequest(
        String category,
        String name,
        String brand,
        String date,
        String condition,
        String description,
        List<String> images,
        ProductDetailRequest details
) {
}
