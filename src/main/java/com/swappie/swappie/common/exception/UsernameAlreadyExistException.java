package com.swappie.swappie.common.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistException extends BaseException {
    public UsernameAlreadyExistException() {
        super(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", "Username already exists");
    }
}
