package com.RentPaymentSystem.app.controller;

import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.dto.UserRegistrationResponse;
import com.RentPaymentSystem.app.dto.UserValidationRequest;
import com.RentPaymentSystem.app.dto.UserValidationResponse;
import com.RentPaymentSystem.app.entity.UserEntity;
import com.RentPaymentSystem.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<UserValidationResponse> validateUser(
            @RequestBody UserValidationRequest request) {
        try {
            UserValidationResponse response = userService.validatePassword(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }


}
