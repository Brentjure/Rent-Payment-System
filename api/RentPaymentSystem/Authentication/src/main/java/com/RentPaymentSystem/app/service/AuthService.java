package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.config.JWTService;
import com.RentPaymentSystem.app.dto.AuthRequest;
import com.RentPaymentSystem.app.dto.AuthResponse;
import com.RentPaymentSystem.app.dto.UserResponse;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthResponse authenticate(AuthRequest authRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, headers);

        // Validate user
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
                "http://localhost:8082/api/v1/users/validate",
                request,
                UserResponse.class
        );
        log.info(response.getBody().toString());

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new BadCredentialsException("User not found!");
        }

        // Generate token
        UserResponse user = response.getBody();
        String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(jwtToken);
    }
}
