package com.nghiangong.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
