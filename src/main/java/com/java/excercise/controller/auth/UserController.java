package com.java.excercise.controller.auth;

import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.dto.response.UserResponse;
import com.java.excercise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/profile")
public class UserController {

    private final UserService userService;


    @GetMapping()
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetails(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getSubject(); // Lấy userId từ JWT
        UserResponse user = userService.getUserDetail(id);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
            .success(true)
            .message("Get User detail successfully")
            .data(user)
            .build();
        return ResponseEntity.ok(response);
    }
}
