package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.NewProductRequest;
import com.java.excercise.mapper.ProductMapper;
import com.java.excercise.service.auth.UserService;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CreateController {
    private final UserService userService;
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> handle(@RequestBody NewProductRequest req, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        var user = userService.getUserById(userId);

        var savedProduct = productService.createProduct(ProductMapper.toEntity(req, user));
        detailService.createDetail(ProductMapper.toDetail(savedProduct, req.details()));

        if (!req.images().isEmpty())
            for (String url : req.images())
                imageService.createImage(ProductMapper.toImage(url, savedProduct));

        var resp = ApiResponse.success("product created", savedProduct.getId());
        return ResponseEntity.ok(resp);
    }

}
