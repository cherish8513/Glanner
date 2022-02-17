package com.glanner.api.exception;

import lombok.Getter;

@Getter
public class DuplicatePlanException extends RuntimeException{
    String userName;
    String title;

    public DuplicatePlanException(){

    }

    public DuplicatePlanException(String userName, String title) {
        this.userName = userName;
        this.title = title;
    }
}
