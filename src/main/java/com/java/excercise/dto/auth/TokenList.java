package com.java.excercise.dto.auth;

public record TokenList(
    String refreshToken,
    String accessToken
) {
}
