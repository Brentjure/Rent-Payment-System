package com.RentPaymentSystem.app.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;


public class RoleAssignmentException extends RuntimeException {
    private HttpStatus status;

    public RoleAssignmentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public RoleAssignmentException(Throwable cause) {}

    public HttpStatus getStatus() {
        return status;
    }
}
