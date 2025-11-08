package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.model.entities.Product;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
//    public ResponseEntity<?> handle(Pageable pageable) {
//        var productPage = productService.getAllProducts(pageable);
//        var respDataPage = productPage
//            .map(product -> FullProductResponse.from(
//                product,
//                detailService.getDetailByProduct(product),
//                imageService.getImagesByProduct(product)
//            ));
//
//        var resp = ApiResponse.success("get all products success", respDataPage);
//        return ResponseEntity.ok(resp);
//    }
    public ResponseEntity<?> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String q // <-- 1. Thêm param 'q'
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage; // Dùng kiểu Page<Product>

        // 2. Logic IF/ELSE
        if (q != null && !q.trim().isEmpty()) {
            // Nếu có 'q' -> Gọi hàm search
            productPage = productService.searchProducts(q, pageable);
        } else {
            // Nếu không có 'q' -> Lấy tất cả (theo phân trang)
            productPage = productService.getAllProducts(pageable);
        }

        // 3. Map từ Page<Product> sang Page<FullProductResponse>
        Page<FullProductResponse> dtoPage = productPage.map(product -> {
            var detail = detailService.getDetailByProduct(product);
            var images = imageService.getImagesByProduct(product);
            return FullProductResponse.from(product, detail, images);
        });
        // 3. Trả về Page<DTO>
        var resp = ApiResponse.success("Get products success", dtoPage);
        return ResponseEntity.ok(resp);
    }
}
