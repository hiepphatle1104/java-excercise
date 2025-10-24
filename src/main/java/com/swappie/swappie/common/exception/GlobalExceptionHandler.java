package com.swappie.swappie.common.exception;

import com.swappie.swappie.common.dto.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AppResponse.error("INTERNAL_SERVER_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(AppResponse.error(ex.getCode(), ex.getMessage()));
    }
}
