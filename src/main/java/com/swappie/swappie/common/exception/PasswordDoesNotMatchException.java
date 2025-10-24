package com.swappie.swappie.common.exception;

import org.springframework.http.HttpStatus;

public class PasswordDoesNotMatchException extends BaseException {
    public PasswordDoesNotMatchException() {
        super(HttpStatus.CONFLICT, "PASSWORD_NOT_MATCH", "Password does not match");
    }
}
