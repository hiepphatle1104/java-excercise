package com.swappie.testproduct;

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

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody NewProductRequest req) {
        // 1️⃣ Kiểm tra product có tồn tại không
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Product not found"));
        }

        Product existingProduct = productOpt.get();

        // 2️⃣ Cập nhật thông tin product
        existingProduct.setName(req.name());
        existingProduct.setPrice(req.price());
        existingProduct.setDescription(req.description());
        productRepository.save(existingProduct);

        // 3️⃣ Cập nhật chi tiết sản phẩm
        ProductDetail productDetail = detailRepository.findByProduct(existingProduct)
                .orElse(new ProductDetail());
        productDetail.setProduct(existingProduct);
        productDetail.setMaximumDistance(req.detail().maximumDistance());
        productDetail.setMotorCapacity(req.detail().motorCapacity());
        productDetail.setWeight(req.detail().weight());
        detailRepository.save(productDetail);

        // 4️⃣ Xóa ảnh cũ (nếu cần) và thêm ảnh mới
        imageRepository.deleteByProduct(existingProduct);
        for (String url : req.images()) {
            imageRepository.save(ProductImage.builder()
                    .url(url)
                    .product(existingProduct)
                    .build());
        }

        // 5️⃣ Trả về phản hồi
        return ResponseEntity.ok(Map.of(
                "message", "Product updated successfully",
                "data", Map.of("productId", existingProduct.getId())
        ));
    }
    
}






