package com.java.excercise.dto.request;

public record SignInRequest(
    String email,
    String password
) {
}
