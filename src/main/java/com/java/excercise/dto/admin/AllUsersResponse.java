package com.java.excercise.dto.admin;

import com.java.excercise.model.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllUsersResponse {
    private String userId;
    private String email;
    private String name;
    private UserStatus status;
    private String url;
}
