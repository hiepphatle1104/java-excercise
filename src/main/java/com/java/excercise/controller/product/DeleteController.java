package com.java.excercise.controller.product;

import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class DeleteController {

    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> handle(@PathVariable String id) {
        var product = productService.getProductById(id);
        detailService.deleteDetailByProduct(product);
        imageService.deleteImagesByProduct(product);
        productService.deleteProduct(product.getId());

        return ResponseEntity.ok().build();
    }
}
