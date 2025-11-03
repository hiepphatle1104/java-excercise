package com.java.excercise.dto.product;

import com.java.excercise.model.Product;
import com.java.excercise.model.ProductDetail;
import com.java.excercise.model.ProductImage;
import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;

import java.util.List;

// Test
public record FullProductResponse(
    String id,
    String name,
    String brand,
    ProductStatus status,
    String description,
    String date,
    ProductCategory category,
    ProductDetail detail,
    List<ProductImage> images
) {
    public static FullProductResponse from(Product product, ProductDetail detail, List<ProductImage> images) {
        return new FullProductResponse(
            product.getId(),
            product.getName(),
            product.getBrand(),
            product.getStatus(),
            product.getDescription(),
            product.getDate(),
            product.getCategory(),
            detail,
            images
        );
    }
}
