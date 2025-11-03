package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.dto.product.UpdateProductRequest;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.model.entities.ProductImage;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    // TODO: Lam product service
    // TODO: Update chua xong
    // TODO: Response get detail & get all chua co userId
    // TODO: Chua co price
    // TODO: Sua image response
    // TODO: Upload image to Cloudinary
    // TODO: Chia controller ra thanh nhieu file
    // TODO: Handle transaction

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
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProductDetailById(@PathVariable String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            ApiResponse errorResponse = ApiResponse.error(
                "product not found", HttpStatus.NOT_FOUND, "PRODUCT NOT FOUND"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Product product = optionalProduct.get();

        ProductDetail productDetail = detailRepository.findByProduct(product).orElse(null);

        List<ProductImage> productImages = imageRepository.findAllByProduct(product);

        FullProductResponse fullProductResponse = FullProductResponse.from(
            product,
            productDetail,
            productImages
        );

        ApiResponse succesReponse = ApiResponse.success("get product detail success", fullProductResponse);

        return ResponseEntity.ok(succesReponse);
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

        product.setName(req.name());
        product.setBrand(req.brand());
        product.setDate(req.date());
        product.setDescription(req.description());
        product.setStatus(ProductStatus.valueOf(req.status()));
        product.setCategory(ProductCategory.valueOf(req.category()));

        Product savedProduct = productRepository.save(product);

        Optional<ProductDetail> detailOpt = detailRepository.findByProduct(product);
        ProductDetail detail;

        if (detailOpt.isPresent()) {

            detail = detailOpt.get();
            detail.setBatteryPercentage(req.details().batteryPercentage());
            detail.setMotorCapacity(req.details().motorCapacity());
            detail.setMaximumDistance(req.details().maximumDistance());
            detail.setChargingTime(req.details().chargingTime());
            detail.setWeight(req.details().weight());
        } else {

            detail = ProductDetail.builder()
                .product(savedProduct)
                .batteryPercentage(req.details().batteryPercentage())
                .motorCapacity(req.details().motorCapacity())
                .maximumDistance(req.details().maximumDistance())
                .chargingTime(req.details().chargingTime())
                .weight(req.details().weight())
                .build();
        }
        detailRepository.save(detail);


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


        ProductDetail updatedDetail = detailRepository.findByProduct(savedProduct).orElse(null);
        List<ProductImage> updatedImages = imageRepository.findAllByProduct(savedProduct);

        FullProductResponse responseData = FullProductResponse.from(savedProduct, updatedDetail, updatedImages);

        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", responseData));
    }

}
