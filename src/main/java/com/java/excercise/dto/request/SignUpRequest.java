package com.java.excercise.dto.request;

public record SignUpRequest(
        String name,
        String email,
        String password,
        String confirmPassword
) {
}
