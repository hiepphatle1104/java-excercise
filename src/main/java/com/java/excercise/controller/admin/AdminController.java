package com.java.excercise.controller.admin;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.dto.admin.AllUsersResponse;
import com.java.excercise.dto.admin.UpdateStatusRequest;
import com.java.excercise.dto.user.UserInfoDTO;
import com.java.excercise.model.enums.UserStatus;
import com.java.excercise.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<AllUsersResponse>>> getAllUsers(
        @RequestParam(required = false) String q, // Thêm param 'q' để search
        Pageable pageable // Thêm Pageable
    ) {
        Page<AllUsersResponse> userPage = adminService.getAllUsers(q, pageable);
        return ResponseEntity.ok(
            ApiResponse.<Page<AllUsersResponse>>builder()
                .success(true)
                .message("Get all users successfully")
                .data(userPage)
                .build()
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id); // Gọi service

        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .success(true)
                .message("User deleted successfully")
                .build()
        );
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> updateState(
        @PathVariable String id,
        @RequestBody UpdateStatusRequest request
        ) {
        adminService.updateState(id, request); // Gọi service

        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .success(true)
                .message("User state updated successfully")
                .build()
        );
    }
}
