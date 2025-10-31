package com.java.excercise.controller.auth;

import com.java.excercise.dto.TokenList;
import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.service.JwtService;
import com.java.excercise.utils.CookieUtils;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

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
        TokenList tokenList = jwtService.createNewToken(refreshToken);

        // Update new token
        ResponseCookie cookie = CookieUtils.createCookie(refreshToken, tokenList.refreshToken());

        var data = Map.of("accessToken", tokenList.accessToken());
        var resp = ApiResponse.success("token refresh success", data);

        // NOTES: Không biết có nên để status CREATED hay không nha ( t nghĩ nên sửa thành ok )
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resp);
    }
}
