package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.repository.DetailRepository;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class DeleteController {

    private final ProductRepository productRepository;
    private final DetailRepository detailRepository;
    private final ImageRepository imageRepository;

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable String id) {

        return productRepository.findById(id)
            .map(product -> {
                detailRepository.findByProduct(product)
                    .ifPresent(detailRepository::delete);

                List<ProductImage> images = imageRepository.findAllByProduct(product);
                if (!images.isEmpty()) {
                    imageRepository.deleteAll(images);
                }

                productRepository.deleteById(id);

                return ResponseEntity.ok(
                    ApiResponse.success("product deleted")
                );
            })
            .orElse(
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("product not found", HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND")
                )
            );
    }
}
