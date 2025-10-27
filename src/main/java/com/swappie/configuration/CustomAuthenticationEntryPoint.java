package com.swappie.configuration;

import com.swappie.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // luôn là UNAUTHENTICATED
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authException.getMessage());
        Throwable cause = authException.getCause();

        boolean success = false;
        String message;
        String errorCode;


        if (cause instanceof JwtException) {
            String causeMessage = cause.getMessage();
            if (causeMessage.contains("expired")) {
                message = "AccessToken has expired";
                errorCode = "ACCESS_EXPIRED";
            } else if (causeMessage.contains("Invalid")) {
                message = "Invalid Token";
                errorCode = "INVALID";
            } else if (causeMessage.contains("Malformed")) {
                message = "Malformed Token";
                errorCode = "MALFORMED";
            } else {
                message = "Invalid token";
                errorCode = "INVALID_TOKEN";
            }
        } else {
            // Xử lý trường hợp cause có thể là null
            // hoặc không phải là JwtException

            if (cause != null) {
                message = authException.getMessage(); // Lấy message từ lỗi gốc
                errorCode = "AUTHENTICATION_FAILED";
            } else {

            // Nếu không có cause, lấy message từ lỗi gốc
                message = authException.getMessage();
                errorCode = "AUTHENTICATION_FAILED";
            }
        }


        ApiResponse apiResponse = ApiResponse.builder()
                .success(success)
                .message(message)
                .errorCode(errorCode)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}