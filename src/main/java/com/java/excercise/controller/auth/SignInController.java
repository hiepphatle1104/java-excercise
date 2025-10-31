package com.java.excercise.controller.auth;

import com.java.excercise.dto.request.SignInRequest;
import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.service.AuthService;
import com.java.excercise.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/signin")
@Slf4j
public class SignInController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> handle(@RequestBody SignInRequest req) {
        var token = authService.login(req);

        // NOTES: Cookie chưa set max-age
        Cookie cookie = CookieUtils.createCookie("refreshToken", token);

        var resp = ApiResponse.success("login success");
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resp);
    }
}
