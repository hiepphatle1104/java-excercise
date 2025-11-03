package com.java.excercise.dto.auth;

public record SignInRequest(
    String email,
    String password
) {
}
