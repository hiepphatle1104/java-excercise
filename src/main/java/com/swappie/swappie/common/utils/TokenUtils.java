package com.swappie.swappie.common.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class TokenUtils {

    private static final SecureRandom random = new SecureRandom();

    public static String generateUUIDToken() {
        return UUID.randomUUID().toString();
    }

    public static String generateSecureToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}