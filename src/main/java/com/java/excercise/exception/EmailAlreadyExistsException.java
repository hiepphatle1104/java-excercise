package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super("email already exists", HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS");
    }
}
