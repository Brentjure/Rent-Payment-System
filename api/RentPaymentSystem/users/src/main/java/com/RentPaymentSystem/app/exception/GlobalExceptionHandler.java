package com.RentPaymentSystem.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserCreationException.class)
    public ResponseEntity<Object> userCreationException(UserCreationException e) {

        HttpStatus status = e.getStatus();
        ApiException userCreationException = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Africa/Nairobi"))
        );

        log.error(userCreationException.getMessage(), userCreationException);

        return new ResponseEntity<>(userCreationException, status);
    }

    @ExceptionHandler(value = RoleAssignmentException.class)
    public ResponseEntity<Object> roleAssignmentException(RoleAssignmentException e) {
        HttpStatus status = e.getStatus();

        ApiException roleAssignmentException = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Africa/Nairobi"))
        );
        log.error(roleAssignmentException.getMessage(), roleAssignmentException);
        return new ResponseEntity<>(roleAssignmentException, status);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UserNotFoundException e) {
        HttpStatus status = e.getStatus();
        ApiException userNotFoundException = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Africa/Nairobi"))
        );
        return new ResponseEntity<>(userNotFoundException, status);
    }


}
