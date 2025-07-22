package com.RentPaymentSystem.app.exception;


import org.springframework.http.HttpStatus;


public class UserCreationException extends RuntimeException {
    private HttpStatus status;

    public UserCreationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


    public UserCreationException(String message, Throwable cause) {}

    public HttpStatus getStatus() {
        return status;
    }
}
