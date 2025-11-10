package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.model.entities.Product;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/me")
@RequiredArgsConstructor
public class GetByUserIdController {
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @GetMapping()
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    public ResponseEntity<?> findAllByUserId(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Product> productList = productService.findAllByUserId(userId);
        var responseData = productList.stream()
            .map(product -> FullProductResponse.from(
                product,
                detailService.getDetailByProduct(product),
                imageService.getImagesByProduct(product)
            ));
        var resp = ApiResponse.success("get all products by userId successfully", responseData);
        return ResponseEntity.ok(resp);
    }

}
