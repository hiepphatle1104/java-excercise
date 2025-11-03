package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException() {
        super("missing or invalid token", HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }
}
