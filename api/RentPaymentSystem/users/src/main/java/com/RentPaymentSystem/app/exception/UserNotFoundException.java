package com.RentPaymentSystem.app.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private HttpStatus status;

    public UserNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
