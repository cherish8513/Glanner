package com.glanner.api.exception;

import lombok.Data;

@Data
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;

    public ErrorResponseEntity(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }
}
