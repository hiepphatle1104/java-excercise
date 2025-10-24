package com.swappie.swappie.common.dto;

public record SignInResponse(
        String accessToken,
        String tokenType,
        String expiresIn
) {
}
