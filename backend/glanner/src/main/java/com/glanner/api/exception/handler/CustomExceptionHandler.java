package com.glanner.api.exception.handler;

import com.glanner.api.exception.ErrorCode;
import com.glanner.api.exception.ErrorResponseEntity;
import com.glanner.api.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
    Exception 처리
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class,
                    UsernameNotFoundException.class,
                    BadCredentialsException.class})
    public ResponseEntity<ErrorResponseEntity> handleNotFoundUser(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.USER_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseEntity> handleAll(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
