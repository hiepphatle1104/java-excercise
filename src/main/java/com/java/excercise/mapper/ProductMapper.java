package com.java.excercise.mapper;

import com.java.excercise.dto.product.NewProductRequest;
import com.java.excercise.dto.product.ProductDetailRequest;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.model.entities.User;
import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;

public class ProductMapper {
    public static Product toEntity(NewProductRequest req, User user) {
        return Product.builder()
            .name(req.name())
            .user(user)
            .brand(req.brand())
            .date(req.date())
            .description(req.description())
            .price(req.price())
            .status(ProductStatus.valueOf(req.status()))
            .category(ProductCategory.valueOf(req.category()))
            .build();
    }

    public static ProductDetail toDetail(Product savedProduct, ProductDetailRequest req) {
        return ProductDetail.builder()
            .product(savedProduct)
            .batteryPercentage(req.batteryPercentage())
            .chargingTime(req.chargingTime())
            .maximumDistance(req.maximumDistance())
            .motorCapacity(req.motorCapacity())
            .weight(req.weight())
            .build();
    }

    public static ProductImage toImage(String url,String publicId, Product savedProduct) {
        return ProductImage.builder()
            .url(url)
            .product(savedProduct)
            .publicId(publicId)
            .build();
    }
}
