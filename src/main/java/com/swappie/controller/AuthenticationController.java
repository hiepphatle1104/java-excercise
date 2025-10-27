package com.swappie.controller;

import com.swappie.dto.request.LoginRequest;
import com.swappie.dto.request.LogoutRequest;
import com.swappie.dto.request.NewTokenRequest;
import com.swappie.dto.response.ApiResponse;
import com.swappie.dto.response.LoginResponse;
import com.swappie.dto.response.NewTokenResponse;
import com.swappie.service.AuthenticationService;
import com.swappie.service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
@Slf4j
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> Login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login Success")
                .data(authenticationService.login(loginRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {
        authenticationService.Logout(logoutRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .success(true)
                .message("Logout Success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auth/createToken")
    public ResponseEntity<ApiResponse<NewTokenResponse>> createNewToken(@RequestBody NewTokenRequest newTokenRequest)
            throws ParseException, JOSEException {
        ApiResponse<NewTokenResponse> response = ApiResponse.<NewTokenResponse>builder()
                .success(true)
                .message("Tokens refreshed successfully")
                .data(jwtService.createNewToken(newTokenRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
