package com.java.excercise.controller.product;

import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.dto.product.NewProductRequest;
import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.model.Product;
import com.java.excercise.model.ProductDetail;
import com.java.excercise.model.ProductImage;
import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;
import com.java.excercise.repository.DetailRepository;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.java.excercise.dto.product.UpdateProductRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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

        Map<String, ProductDetail> detailMap = detailRepository.findAll().stream()
            .collect(Collectors.toMap(detail -> detail.getProduct().getId(), detail -> detail));

        Map<String, List<ProductImage>> imageMap = imageRepository.findAll().stream()
            .collect(Collectors.groupingBy(image -> image.getProduct().getId()));

        List<FullProductResponse> responseData = products.stream().map(product -> FullProductResponse.from(
                product,
                detailMap.get(product.getId()),
                imageMap.get(product.getId())
            ))
            .collect(Collectors.toList());

        var resp = ApiResponse.success("get all products success", responseData);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
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

        // TODO: Sua lai response
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
            .status(ProductStatus.valueOf(req.status()))
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

        // TODO: Sua lai response
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "message", "product created",
            "data", Map.of("productId", savedProduct.getId())
        ));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable String id) {

        return productRepository.findById(id)
            .map(product -> {
                detailRepository.findByProduct(product)
                    .ifPresent(detailRepository::delete);

                List<ProductImage> images = imageRepository.findAllByProduct(product);
                if (!images.isEmpty()) {
                    imageRepository.deleteAll(images);
                }
                productRepository.deleteById(id);
                return ResponseEntity.ok(
                    ApiResponse.success("product deleted")
                );
            })
            .orElse(
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("product not found", HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND")
                )
            );
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProduct(
        @PathVariable String id,
        @RequestBody UpdateProductRequest req
    ) {

        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Product not found", HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND"));
        }

        Product product = productOpt.get();

        if (req.name() != null) {
            product.setName(req.name());
        }
        if (req.brand() != null) {
            product.setBrand(req.brand());
        }
        if (req.date() != null) {
            product.setDate(req.date());
        }
        if (req.description() != null) {
            product.setDescription(req.description());
        }
        if (req.status() != null) {
            product.setStatus(ProductStatus.valueOf(req.status()));
        }
        if (req.category() != null) {
            product.setCategory(ProductCategory.valueOf(req.category()));
        }

        Product savedProduct = productRepository.save(product);

        Optional<ProductDetail> detailOpt = detailRepository.findByProduct(product);
        ProductDetail detail;

        if (detailOpt.isPresent()) {
            detail = detailOpt.get();
            if (req.details() != null) {
                if (req.details().batteryPercentage() != null) {
                    detail.setBatteryPercentage(req.details().batteryPercentage());
                }
                if (req.details().motorCapacity() != null) {
                    detail.setMotorCapacity(req.details().motorCapacity());
                }
                if (req.details().maximumDistance() != null) {
                    detail.setMaximumDistance(req.details().maximumDistance());
                }
                if (req.details().chargingTime() != null) {
                    detail.setChargingTime(req.details().chargingTime());
                }
                if (req.details().weight() != null) {
                    detail.setWeight(req.details().weight());
                }
            }
        } else if (req.details() != null) {

            detail = ProductDetail.builder()
                .product(savedProduct)
                .batteryPercentage(req.details().batteryPercentage())
                .motorCapacity(req.details().motorCapacity())
                .maximumDistance(req.details().maximumDistance())
                .chargingTime(req.details().chargingTime())
                .weight(req.details().weight())
                .build();
        } else {
            detail = null;
        }

        if (detail != null) {
            detailRepository.save(detail);
        }


        if (req.images() != null) {
            List<ProductImage> existingImages = imageRepository.findAllByProduct(product);
            if (!existingImages.isEmpty()) {
                imageRepository.deleteAll(existingImages);
            }

            for (String url : req.images()) {
                imageRepository.save(ProductImage.builder()
                    .url(url)
                    .product(savedProduct)
                    .build());
            }
        }

        ProductDetail updatedDetail = detailRepository.findByProduct(savedProduct).orElse(null);
        List<ProductImage> updatedImages = imageRepository.findAllByProduct(savedProduct);

        FullProductResponse responseData = FullProductResponse.from(savedProduct, updatedDetail, updatedImages);

        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", responseData));
    }
    }






