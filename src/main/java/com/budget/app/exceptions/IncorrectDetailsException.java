package com.budget.app.exceptions;

public class IncorrectDetailsException extends RuntimeException {

    public IncorrectDetailsException(String message) {
        super(message);
    }
}
