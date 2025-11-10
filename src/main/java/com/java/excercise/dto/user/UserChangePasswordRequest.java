package com.java.excercise.dto.user;

import lombok.Data;

@Data
public class UserChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
