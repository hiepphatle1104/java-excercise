package com.swappie.testproduct.dto;

import java.util.List;

public record NewProductRequest<date>(
        String category,
        String name,
        String branch
        String date,
        String condition,
        String description,
        List<String> images,
        ProductDetailRequest details
) {
}
