package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserPassword extends BaseException {
    public InvalidUserPassword() {
        super("Invalid user password", HttpStatus.BAD_REQUEST, "INVALID_USER_PASSWORD");
    }
}
