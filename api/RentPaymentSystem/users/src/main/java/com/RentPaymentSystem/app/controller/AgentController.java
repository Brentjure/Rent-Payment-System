package com.RentPaymentSystem.app.controller;

import com.RentPaymentSystem.app.dto.ApiResponse;
import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.service.AgentUserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent/")
@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
public class AgentController {
    private final AgentUserRegistrationService agentUserRegistrationService;

    @PostMapping("register/tenant")
    public ResponseEntity<ApiResponse<String>> registerTenant(@RequestBody UserRegistrationRequest request) {
        agentUserRegistrationService.registerTenant(request);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Tenant registered successfully!",
                null,
                200);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("register/landlord")
    public ResponseEntity<ApiResponse<String>> registerLandlord(@RequestBody UserRegistrationRequest request) {
        agentUserRegistrationService.registerLandlord(request);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                "LandLord registered successfully!",
                null,
                200);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("register/caretaker")
    public ResponseEntity<ApiResponse<String>> registerCaretaker(@RequestBody UserRegistrationRequest request) {
        agentUserRegistrationService.registerCareTaker(request);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Caretaker registered successfully!",
                null,
                200);
        return ResponseEntity.ok(apiResponse);
    }
}
