package com.java.excercise.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Set<String> roles;
}
