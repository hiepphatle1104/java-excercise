package com.java.excercise.controller.cart;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.cart.AddCartItemRequest;
import com.java.excercise.dto.cart.CartItemDTO;
import com.java.excercise.dto.cart.CartResponse;
import com.java.excercise.dto.user.UserInfoDTO;
import com.java.excercise.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/item")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody AddCartItemRequest addCartItemRequest
        ) {
        String id = jwt.getSubject(); // Lấy userId từ JWT

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
            .success(true)
            .message("Add item to cart successfully")
            .data(cartService.addItemToCart(id, addCartItemRequest))
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
        @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
            .success(true)
            .message("Get cart successfully")
            .data(cartService.getCartByUserId(userId))
            .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> deleteItemFromCart(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String productId
    ) {
        String id = jwt.getSubject(); // Lấy userId từ JWT

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
            .success(true)
            .message("Add item to cart successfully")
            .data(cartService.deleteItemFromCart(id, productId))
            .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/item/quantity")
    public  ResponseEntity<ApiResponse<CartResponse>> updateItemInCart(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody AddCartItemRequest request
    ) {
        String id = jwt.getSubject(); // Lấy userId từ JWT

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
            .success(true)
            .message("Update item quantity successfully")
            .data(cartService.updateItemInCart(id, request))
            .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/all") // Endpoint để xoá tất cả
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
        @AuthenticationPrincipal Jwt jwt
    ) {
        String id = jwt.getSubject(); // Lấy userId từ JWT

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
            .success(true)
            .message("Cart cleared successfully")
            .data(cartService.clearCart(id))
            .build();
        return ResponseEntity.ok(response);
    }
}
