package com.swappie.testproduct.dto;

import com.swappie.testproduct.enums.ProductCategory;
import com.swappie.testproduct.enums.ProductCondition;
import com.swappie.testproduct.model.Product;
import com.swappie.testproduct.model.ProductDetail;
import com.swappie.testproduct.model.ProductImage;

import java.util.List;

public record FullProductReponse (
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
