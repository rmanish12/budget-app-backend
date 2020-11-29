package com.budget.app.exceptionHandler;

import com.budget.app.exceptions.AlreadyPresentException;
import com.budget.app.exceptions.IncorrectDetailsException;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.Response;
import com.budget.app.responseMessage.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Response> alreadyPresentExceptionHandler(AlreadyPresentException e) {

        Response error = new Response();
        error.setTimestamp(LocalDateTime.now());
        error.setStatusCode(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler
    public ResponseEntity<Response> badCredentialsExceptionHandler(BadCredentialsException e) {

        Response error = new Response();
        error.setTimestamp(LocalDateTime.now());
        error.setStatusCode(HttpStatus.FORBIDDEN.value());
        error.setMessage(ResponseMessage.BAD_CREDENTIALS.toString());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    public ResponseEntity<Response> notFoundExceptionHandler(NotFoundException e) {

        Response error = new Response();
        error.setMessage(e.getMessage());
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Response> incorrectDetailsExceptionHandler(IncorrectDetailsException e) {

        Response error = new Response();
        error.setTimestamp(LocalDateTime.now());
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<Response> exceptionHandler(Exception e) {

        Response error = new Response();
        error.setTimestamp(LocalDateTime.now());
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ResponseMessage.SERVER_ERROR.toString());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
