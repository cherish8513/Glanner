package com.glanner.api.exception.handler;

import com.glanner.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundBoard(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.BOARD_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundComment(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.COMMENT_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GlannerNotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundGlanner(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.GlANNER_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DailyWorkNotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundDailyWork(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.DAILY_WORK_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConferenceNotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundConference(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.CONFERENCE_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponseEntity> handleDuplicatedMember(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.DUPLICATE_MEMBER);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(DuplicatePlanException.class)
    public ResponseEntity<PlanErrorResponseEntity> handleDuplicatedPlan(DuplicatePlanException ex){
        PlanErrorResponseEntity response = new PlanErrorResponseEntity(ErrorCode.DUPLICATE_PLAN, ex.getUserName(), ex.getTitle());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(MailNotSentException.class)
    public ResponseEntity<ErrorResponseEntity> handleSentMailError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.MAIL_NOT_SENT);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(SMSNotSentException.class)
    public ResponseEntity<ErrorResponseEntity> handleSentSMSError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.SMS_NOT_SENT);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(FileNotSavedException.class)
    public ResponseEntity<ErrorResponseEntity> handleSavedFileError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.FILE_NOT_SAVED);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(FullUserInGroupException.class)
    public ResponseEntity<ErrorResponseEntity> handleFullUserError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.FULL_USER_IN_GROUP);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(AlreadyInGroupException.class)
    public ResponseEntity<ErrorResponseEntity> handleAlreadyInGroupError(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.ALREADY_IN_GROUP);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseEntity> handleAll(Exception ex){
        ErrorResponseEntity response = new ErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
