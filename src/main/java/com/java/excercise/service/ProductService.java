package com.java.excercise.service;

import lombok.RequiredArgsConstructor;
import com.java.excercise.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // tạo một sản phẩm
    public Product create(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ApiError("Product already exists", HttpStatus.BAD_REQUEST, "PRODUCT_ALREADY_EXISTS");
        }
        return productRepository.save(product);
    }

    //lấy ra tất cả sản phẩm
    // phn quyền chỉ có admin mới lấy ra được toàn bộ sản phẩm
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    // lấy sản phẩm theo id
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiError("product not found", HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND"));
    }


}
