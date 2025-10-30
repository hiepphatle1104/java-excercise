package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException() {
        super("invalid token", HttpStatus.BAD_REQUEST, "INVALID_TOKEN");
    }
}
