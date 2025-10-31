package com.java.excercise.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    public static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");

        return cookie;
    }

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = createCookie(name, value);
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public static Cookie deleteCookie(String name) {
        return createCookie(name, "", 0);
    }
}
