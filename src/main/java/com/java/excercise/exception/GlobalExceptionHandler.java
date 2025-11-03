package com.java.excercise.exception;

import com.java.excercise.dto.ApiResponse;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex) {

        var resp = ApiResponse.error(ex.getMessage(), ex.getStatus(), ex.getErrorCode());
        return ResponseEntity.status(ex.getStatus()).body(resp);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception ex) {
//        var resp = ApiResponse.error(
//            "server error",
//            HttpStatus.INTERNAL_SERVER_ERROR,
//            "INTERNAL_SERVER_ERROR"
//        );
//
//        return ResponseEntity.status(resp.getStatus()).body(resp);
//    }

    // TODO: Need to remove error handler below
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiResponse> handleException(ApiError error) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message(error.getMessage())
            .errorCode(error.getErrorCode())
            .build();
        return ResponseEntity
            .status(error.getStatus())
            .body(response);
    }

    // Handler cho việc verify token
    @ExceptionHandler({ParseException.class, JOSEException.class})
    public ResponseEntity<ApiResponse> handleJwtTechnicalErrors(Exception e) {
        String message;

        // Phân loại lỗi kỹ thuật
        if (e instanceof ParseException) {
            // TOKEN GỬI LÊN SAI ĐỊNH DẠNG HEADER.PAYLOAD.SIGNATURE
            message = "Malformed Token.";
        } else {
            // LỖI KHÁC NHƯ LÀ LỖI THUẬT TOÁN XÁC THỰC KHÔNG KHỚP CHẲNG HẠN.....
            message = "Technical error while validating refresh token.";
        }

        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message(message)
            .errorCode("MALFORMED")
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response);
    }

    // Handler cho việc authorize
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message("You do not have permission to access this resource.")
            .errorCode("FORBIDDEN")
            .build();

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(response);
    }

}
