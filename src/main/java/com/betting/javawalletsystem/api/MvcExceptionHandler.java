package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.ErrorDto;
import com.betting.javawalletsystem.exception.TransactionExistsException;
import com.betting.javawalletsystem.exception.UnauthorisedException;
import com.betting.javawalletsystem.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {
    private final HttpHeaders httpHeaders;

    public MvcExceptionHandler(){
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(TransactionExistsException.class)
    public ResponseEntity<ErrorDto> transactionExistsExceptionHandler(TransactionExistsException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<ErrorDto> unauthorisedExceptionHandler(UnauthorisedException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> userNotFoundExceptionHandler(UserNotFoundException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.NOT_FOUND);
    }
}
