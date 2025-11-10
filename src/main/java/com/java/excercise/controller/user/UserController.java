package com.java.excercise.controller.user;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.auth.UserResponse;
import com.java.excercise.dto.user.UserInfoDTO;
import com.java.excercise.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
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

    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoDTO>> createUserInfo(
        @RequestBody UserInfoDTO userInfoDTO,
        @AuthenticationPrincipal Jwt jwt
    ) {
        String id = jwt.getSubject(); // Lấy userId từ JWT
        UserInfoDTO user = userService.updateUserInfo(id, userInfoDTO);

        ApiResponse<UserInfoDTO> response = ApiResponse.<UserInfoDTO>builder()
            .success(true)
            .message("Create user info successfully")
            .data(user)
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoDTO>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getSubject(); // Lấy userId từ JWT

        ApiResponse<UserInfoDTO> response = ApiResponse.<UserInfoDTO>builder()
            .success(true)
            .message("Get User detail successfully")
            .data(userService.getUserInfo(id))
            .build();
        return ResponseEntity.ok(response);
    }
}
