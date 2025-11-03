package com.java.excercise.dto.auth;

public record SignUpRequest(
    String name,
    String email,
    String password,
    String confirmPassword
) {
}
