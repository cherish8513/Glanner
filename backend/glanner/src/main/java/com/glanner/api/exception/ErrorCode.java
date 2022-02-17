package com.glanner.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"404 - ERROR", "Page not found"),
    USER_NOT_FOUND(404, "404 - ERROR", "User not found"),
    BOARD_NOT_FOUND(404, "404 - ERROR", "Board not found"),
    COMMENT_NOT_FOUND(404, "404 - ERROR", "Comment not found"),
    GlANNER_NOT_FOUND(404, "404 - ERROR", "Glanner not found"),
    DAILY_WORK_NOT_FOUND(404, "404 - ERROR", "Daily work not found"),
    CONFERENCE_NOT_FOUND(404, "404 - ERROR", "Conference work not found"),

    DUPLICATE_MEMBER(500, "501 - ERROR", "Duplicated member"),
    DUPLICATE_PLAN(500, "501 - ERROR", "Duplicated plan"),
    METHOD_ARGUMENT_NOT_VALID(500, "502 - ERROR", "Method argument not valid"),
    MAIL_NOT_SENT(500, "503 - ERROR", "Mail not sent"),
    SMS_NOT_SENT(500, "504 - ERROR", "SMS not sent"),
    FILE_NOT_SAVED(500, "505 - ERROR", "File not saved"),
    FULL_USER_IN_GROUP(500, "506 - ERROR", "Full user in group"),
    ALREADY_IN_GROUP(500, "507 - ERROR", "Already in group"),

    INTERNAL_SERVER_ERROR(500, "500 - ERROR", "Internal server error");

    private int status;
    private String errorCode;
    private String message;
}
