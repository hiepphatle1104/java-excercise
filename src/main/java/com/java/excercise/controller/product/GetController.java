package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.repository.DetailRepository;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetController {

    private final ProductRepository productRepository;
    private final DetailRepository detailRepository;
    private final ImageRepository imageRepository;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProductDetailById(@PathVariable String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            ApiResponse errorResponse = ApiResponse.error(
                "product not found", HttpStatus.NOT_FOUND, "PRODUCT NOT FOUND"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Product product = optionalProduct.get();

        ProductDetail productDetail = detailRepository.findByProduct(product).orElse(null);

        List<ProductImage> productImages = imageRepository.findAllByProduct(product);

        FullProductResponse fullProductResponse = FullProductResponse.from(
            product,
            productDetail,
            productImages
        );

        ApiResponse succesReponse = ApiResponse.success("get product detail success", fullProductResponse);

        return ResponseEntity.ok(succesReponse);
    }
}
