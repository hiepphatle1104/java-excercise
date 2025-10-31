package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class TokenMissingException extends BaseException {
    public TokenMissingException() {
        super("token missing", HttpStatus.BAD_REQUEST, "MISSING_TOKEN");
    }
}
