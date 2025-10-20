package com.swappie.authservice.helper;

import java.util.UUID;

public class TokenGenerator {
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
