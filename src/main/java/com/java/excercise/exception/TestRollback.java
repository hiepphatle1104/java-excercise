package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class TestRollback extends BaseException {

    public TestRollback() {
        super("test rollback", HttpStatus.BAD_REQUEST, "TEST_ROLLBACK");
    }
}
