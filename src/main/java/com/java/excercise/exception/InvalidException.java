package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class InvalidException extends BaseException {
    public InvalidException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}
