package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}
