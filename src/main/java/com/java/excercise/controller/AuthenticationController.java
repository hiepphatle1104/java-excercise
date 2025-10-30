package com.java.excercise.controller;

import com.java.excercise.dto.request.LoginRequest;
import com.java.excercise.dto.request.LogoutRequest;
import com.java.excercise.dto.request.NewTokenRequest;
import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.dto.response.LoginResponse;
import com.java.excercise.dto.response.NewTokenResponse;
import com.java.excercise.service.AuthenticationService;
import com.java.excercise.service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> Login(@RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .build();

        loginResponse.setRefreshToken("");

        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login Success")
                .data(loginResponse)
                .build();


        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }


    @PostMapping("/auth/signout")
    public ResponseEntity<ApiResponse> logout(
            // yêu cầu string trích xuất cookie
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) throws ParseException, JOSEException {
        authenticationService.Logout(refreshToken);

        // vô hiệu hóa cookie có chứa RT
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // Phải giống lúc tạo
                .path("/")
                .maxAge(0)     // Hết hạn ngay lập tức
                .build();

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Logout Success")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<NewTokenResponse>> createNewToken(
            @CookieValue(name = "refreshToken", required = false ) String refreshToken
    ) throws ParseException, JOSEException {
        NewTokenResponse newToken = jwtService.createNewToken(refreshToken);

        // update lại cookie chứa RT cũ hiện tại thành RT mới
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .build();

        newToken.setRefreshToken("");
        ApiResponse<NewTokenResponse> response = ApiResponse.<NewTokenResponse>builder()
                .success(true)
                .message("Tokens refreshed successfully")
                .data(newToken)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
    
}
