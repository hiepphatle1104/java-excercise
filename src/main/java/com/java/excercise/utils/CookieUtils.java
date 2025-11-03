package com.java.excercise.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    public static ResponseCookie createCookie(String name, String value) {
        return ResponseCookie
            .from(name)
            .value(value)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .build();
    }

    public static ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie
            .from(name)
            .value(value)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .build();
    }

    public static ResponseCookie deleteCookie(String name) {
        return ResponseCookie
            .from(name)
            .value("")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .build();
    }
}
