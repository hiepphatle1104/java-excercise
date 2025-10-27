package com.swappie.controller;

import com.swappie.dto.response.ApiResponse;
import com.swappie.model.Product;
import com.swappie.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> create (@Valid @RequestBody Product product) {
        ApiResponse<Product> response = ApiResponse.<Product>builder()
                .success(true)
                .message("Product created successfully")
                .data(productService.create(product))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> findAll() {
        // checkd role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("infor user: " + authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info(
                authority.getAuthority()
        ));
        ApiResponse<List<Product>> response = ApiResponse.<List<Product>>builder()
                .success(true)
                .message("Get All Products successfully")
                .data(productService.findAllProduct())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> findById(@PathVariable String id) {
        ApiResponse<Product> response = ApiResponse.<Product>builder()
                .success(true)
                .message("Found Product successfully")
                .data(productService.findById(id))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
