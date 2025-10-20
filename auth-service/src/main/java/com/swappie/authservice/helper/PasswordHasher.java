package com.swappie.authservice.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return ENCODER.encode(plainPassword);
    }

    public static boolean matchPassword(String plainPassword, String hashedPassword) {
        return ENCODER.matches(plainPassword, hashedPassword);
    }
}
