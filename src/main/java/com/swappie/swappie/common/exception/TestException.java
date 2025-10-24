package com.swappie.swappie.common.exception;

import org.springframework.http.HttpStatus;

public class TestException extends BaseException {
    public TestException() {
        super(HttpStatus.BAD_REQUEST, "TEST_EXCEPTION", "Test Exception");
    }
}
