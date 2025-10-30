package com.java.excercise.dto.response;

import com.java.excercise.model.User;

import java.util.Set;

public record SignUpResponse(
        String id,
        String name,
        String email,
        Set<String> roles
) {
    public static SignUpResponse map(User user) {
        return new SignUpResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    }
}
