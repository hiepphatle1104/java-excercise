package com.java.excercise.controller.product;

import com.java.excercise.dto.product.NewProductRequest;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;
import com.java.excercise.repository.DetailRepository;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CreateController {
    private final ProductRepository productRepository;
    private final DetailRepository detailRepository;
    private final ImageRepository imageRepository;

    @PostMapping
    public ResponseEntity<?> handle(@RequestBody NewProductRequest req, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        Product product = Product.builder()
            .name(req.name())
            .userId(userId)
            .brand(req.brand())
            .date(req.date())
            .description(req.description())
            .price(req.price()) //them price tu req gan vao product
            .status(ProductStatus.valueOf(req.status()))
            .category(ProductCategory.valueOf(req.category()))
            .build();

        var savedProduct = productRepository.save(product);

        var productDetail = req.details();
        ProductDetail detail = ProductDetail.builder()
            .product(savedProduct)
            .batteryPercentage(productDetail.batteryPercentage())
            .chargingTime(productDetail.chargingTime())
            .maximumDistance(productDetail.maximumDistance())
            .motorCapacity(productDetail.motorCapacity())
            .weight(productDetail.weight())
            .build();
        detailRepository.save(detail);

        for (String url : req.images())
            imageRepository.save(ProductImage.builder()
                .url(url)
                .product(savedProduct)
                .build());

        // TODO: Sua lai response
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "message", "product created",
            "data", Map.of("productId", savedProduct.getId())
        ));
    }

}
