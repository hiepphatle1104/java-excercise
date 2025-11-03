package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetAllController {
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<?> handle() {
        var products = productService.getAllProducts();
        var respData = products
            .stream()
            .map(product -> FullProductResponse.from(
                product,
                detailService.getDetailByProduct(product),
                imageService.getImagesByProduct(product)
            ));

        var resp = ApiResponse.success("get all products success", respData);
        return ResponseEntity.ok(resp);
    }
}
