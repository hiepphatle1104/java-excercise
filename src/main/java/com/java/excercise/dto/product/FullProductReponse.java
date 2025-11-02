package com.java.excercise.dto.product;

import com.java.excercise.model.Product;
import com.java.excercise.model.ProductDetail;
import com.java.excercise.model.ProductImage;
import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductCondition;

import java.util.List;

public record FullProductReponse(
    String id,
    String name,
    String brand,
    ProductCondition condition,
    String description,
    String data,
    ProductCategory category,
    ProductDetail detail,
    List<ProductImage> images
) {
    public static FullProductReponse from(Product product, ProductDetail detail, List<ProductImage> images) {
        return new FullProductReponse(
            product.getId(),
            product.getName(),
            product.getBrand(),
            product.getCondition(),
            product.getDescription(),
            product.getDate(),
            product.getCategory(),
            detail,
            images
        );
    }
}
