package com.java.excercise.controller.product;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.product.NewProductRequest;
import com.java.excercise.exception.UploadfaileExeption;
import com.java.excercise.mapper.ProductMapper;
import com.java.excercise.service.CloudinaryService;
import com.java.excercise.service.auth.UserService;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import com.java.excercise.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CreateController {
    private final UserService userService;
    private final ProductService productService;
    private final DetailService detailService;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;

//    @PostMapping
//    @Transactional
//    public ResponseEntity<?> handle(@RequestBody NewProductRequest req, @AuthenticationPrincipal Jwt jwt) {
//        String userId = jwt.getSubject();
//        var user = userService.getUserById(userId);
//
//        var savedProduct = productService.createProduct(ProductMapper.toEntity(req, user));
//        detailService.createDetail(ProductMapper.toDetail(savedProduct, req.details()));
//
//        if (!req.images().isEmpty())
//            for (String url : req.images())
//                imageService.createImage(ProductMapper.toImage(url, savedProduct));
//
//        var resp = ApiResponse.success("product created", savedProduct.getId());
//        return ResponseEntity.ok(resp);
//    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> handle(
        // 1. Nhận data JSON (giống hệt NewProductRequest)
        @RequestPart(value = "product") NewProductRequest req,
        // 2. Nhận danh sách file ảnh
        @RequestPart(value = "images", required = false)List<MultipartFile> images,
        @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        var user = userService.getUserById(userId);

        var savedProduct = productService.createProduct(ProductMapper.toEntity(req, user));
        detailService.createDetail(ProductMapper.toDetail(savedProduct, req.details()));

        // 3. Xử lý ảnh
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                try {
                    // 5. Gọi cloudinary service để upload ảnh và lấy url về
                    String imageUrl = cloudinaryService.uploadFile(file);

                    // 6. Lưu Url này vào db như logic cũ
                    imageService.createImage(ProductMapper.toImage(imageUrl, savedProduct));
                } catch (Exception e) {
                    throw new UploadfaileExeption();
                }
            }
        }

        var resp = ApiResponse.success("product created", savedProduct.getId());
        return ResponseEntity.ok(resp);
    }

}
