package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super("invalid credentials", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
    }
}
