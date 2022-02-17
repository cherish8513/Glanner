package com.glanner.api.exception;

import lombok.Getter;

@Getter
public class PlanErrorResponseEntity extends ErrorResponseEntity{
    String userName;
    String title;

    public PlanErrorResponseEntity(ErrorCode errorCode, String userName, String title) {
        super(errorCode);
        this.userName = userName;
        this.title = title;
    }
}
