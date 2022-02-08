package com.glanner.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"404 - ERROR", "Page not found"),
    INTERNAL_SERVER_ERROR(500, "500 - ERROR", "Internal server error"),
    USER_NOT_FOUND(404, "404 - ERROR", "User not found");
    private int status;
    private String errorCode;
    private String message;
}
