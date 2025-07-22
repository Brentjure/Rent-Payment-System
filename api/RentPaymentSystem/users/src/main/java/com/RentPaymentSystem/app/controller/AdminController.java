package com.RentPaymentSystem.app.controller;

import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.UserEntity;
import com.RentPaymentSystem.app.service.AdminService;
import com.RentPaymentSystem.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_AGENT')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;


    @GetMapping("")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/users/agents")
    public ResponseEntity<String> registerAgent(@RequestBody UserRegistrationRequest request) {
        adminService.registerAgent(request);
        return ResponseEntity.ok("Agent registered successfully.");
    }

}
