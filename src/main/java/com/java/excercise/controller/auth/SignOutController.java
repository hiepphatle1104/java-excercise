package com.java.excercise.controller.auth;

import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.service.AuthService;
import com.java.excercise.utils.CookieUtils;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/signout")
@Slf4j
public class SignOutController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> handle(
        // yêu cầu string trích xuất cookie
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) throws ParseException, JOSEException {
        authService.logout(refreshToken);

        // Delete cookie
        Cookie cookie = CookieUtils.deleteCookie("refreshToken");

        var response = ApiResponse.success("logout success");
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
