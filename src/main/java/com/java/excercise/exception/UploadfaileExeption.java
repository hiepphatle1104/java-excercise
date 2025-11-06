package com.java.excercise.exception;

import org.springframework.http.HttpStatus;

public class UploadfaileExeption extends BaseException {
    public UploadfaileExeption() {
        super("Upload failed", HttpStatus.BAD_REQUEST, "UPLOAD_FAILED");
    }
}
