package com.java.excercise.controller.auth;

import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.service.JwtService;
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
@RequestMapping("/api/auth")
@Slf4j
public class TokenController {
    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<?> handle(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) throws ParseException, JOSEException {
        var token = jwtService.createNewToken(refreshToken);

        // Update new token
        Cookie cookie = CookieUtils.createCookie(refreshToken, token);
        var resp = ApiResponse.success("token refresh success");

        // NOTES: Không biết có nên để status CREATED hay không nha ( t nghĩ nên sửa thành ok )
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resp);
    }
}
