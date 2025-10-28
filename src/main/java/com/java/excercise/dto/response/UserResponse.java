package com.java.excercise.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String name;
    private String email;
    private Set<String> roles;
}
