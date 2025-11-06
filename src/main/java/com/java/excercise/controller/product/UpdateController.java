package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.dto.product.UpdateProductRequest;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class UpdateController {
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProduct(
        @PathVariable String id,
        @RequestBody @Valid UpdateProductRequest req
    ) {
        Product product = productService.updateProduct(id, req);

        ProductDetail detail = detailService.updateDetail(product, req.getDetails());

        List<String> images = imageService.updateImage(product, req.getImages());

        FullProductResponse responseData = FullProductResponse.from(product, detail, images);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", responseData));
    }
}
