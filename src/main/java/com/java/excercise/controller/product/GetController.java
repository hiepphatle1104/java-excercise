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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetController {
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> handle(@PathVariable String id) {
        var product = productService.getProductById(id);
        var detail = detailService.getDetailByProduct(product);
        var images = imageService.getImagesByProduct(product);

        var respData = FullProductResponse.from(product, detail, images);
        var resp = ApiResponse.success("get product success", respData);
        return ResponseEntity.ok(resp);
    }
}
