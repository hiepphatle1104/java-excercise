package com.swappie.swappie.common.exception;

import org.springframework.http.HttpStatus;

public class SessionAlreadyExistException extends BaseException {
    public SessionAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "SESSION_ALREADY_EXISTS", "session already exists");
    }
}
