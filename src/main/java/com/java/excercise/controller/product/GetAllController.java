package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.FullProductResponse;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.repository.DetailRepository;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetAllController {

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

        List<FullProductResponse> responseData = products.stream()
            .map(product -> FullProductResponse.from(
                product,
                detailMap.get(product.getId()),
                imageMap.get(product.getId())
            ))
            .collect(Collectors.toList());

        var resp = ApiResponse.success("get all products success", responseData);

        return ResponseEntity.ok(resp);
    }
}
