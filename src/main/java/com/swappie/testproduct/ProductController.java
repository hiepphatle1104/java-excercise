package com.swappie.testproduct;

import com.swappie.testproduct.dto.ApiResponse;
import com.swappie.testproduct.dto.NewProductRequest;
import com.swappie.testproduct.enums.ProductCategory;
import com.swappie.testproduct.enums.ProductCondition;
import com.swappie.testproduct.model.Product;
import com.swappie.testproduct.model.ProductDetail;
import com.swappie.testproduct.model.ProductImage;
import com.swappie.testproduct.repository.DetailRepository;
import com.swappie.testproduct.repository.ImageRepository;
import com.swappie.testproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final DetailRepository detailRepository;
    private final ImageRepository imageRepository;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id) {
        // TODO: Fix show product on fetch
        Optional<Product> result = productRepository.findById(id);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        Optional<ProductDetail> detailResult = detailRepository.findByProduct(result.get());
        if (detailResult.isEmpty())
            return ResponseEntity.notFound().build();

        List<ProductImage> imageList = imageRepository.findAllByProduct(result.get());
        if (imageList.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "info", result.get(),
                "detail", detailResult.get(),
                "images", imageList

        ));
    }


    @PostMapping
    public ResponseEntity<?> handle(@RequestBody NewProductRequest req) {
        Product product = Product.builder()
                .name(req.name())
                .brand(req.brand())
                .date(req.date())
                .description(req.description())
                .condition(ProductCondition.valueOf(req.condition()))
                .category(ProductCategory.valueOf(req.category()))
                .build();

        var savedProduct = productRepository.save(product);

        var productDetail = req.details();
        ProductDetail detail = ProductDetail.builder()
                .product(savedProduct)
                .batteryPercentage(productDetail.batteryPercentage())
                .chargingTime(productDetail.chargingTime())
                .maximumDistance(productDetail.maximumDistance())
                .motorCapacity(productDetail.motorCapacity())
                .weight(productDetail.weight())
                .build();
        detailRepository.save(detail);

        for (String url : req.images())
            imageRepository.save(ProductImage.builder()
                    .url(url)
                    .product(savedProduct)
                    .build());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "product created",
                "data", Map.of("productId", savedProduct.getId())
        ));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String id) {

        //tìm thực thể chính(Product)
        return productRepository.findById(id)
                .map(product -> {
                    //xóa thực thể phụ thuộc ProductDetail (nếu có)
                    detailRepository.findByProduct(product)
                            .ifPresent(detailRepository::delete);

                    //xóa thực thể phụ thuộc ProductImage (nếu có)
                    List<ProductImage> images = imageRepository.findAllByProduct(product);
                    if (!images.isEmpty()) {
                        imageRepository.deleteAll(images);
                    }

                    //xóa thực thể chính (Product)
                    productRepository.deleteById(id);

                    //tạo và trả về JSON báo thành công (200 OK)
                    ApiResponse response = ApiResponse.builder()
                            .success(true)
                            .message("delete successfully")
                            .data(null)
                            .build();

                    return ResponseEntity.ok(response);
                })
                .orElse(
                        //nếu tìm thực thể chính tất bại sẽ tạo và trả về JSON báo lỗi (404 Not Found)
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                // ĐÃ SỬA: "success" (2 chữ 'c')
                                ApiResponse.builder()
                                        .success(false)
                                        .message("Product not found with id: " + id)
                                        .data(null)
                                        .build()
                        )
                );
    }
}