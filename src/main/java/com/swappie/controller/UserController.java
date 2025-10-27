package com.swappie.controller;

import com.swappie.dto.request.UserRequest;
import com.swappie.dto.response.ApiResponse;
import com.swappie.dto.response.UserResponse;
import com.swappie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserService Userservice;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest userRequest) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .data(Userservice.createUser(userRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
