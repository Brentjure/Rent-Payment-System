package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.Role;
import com.RentPaymentSystem.app.entity.UserEntity;
import com.RentPaymentSystem.app.oauth2.KeyCloakTokenService;
import com.RentPaymentSystem.app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final KeyCloakTokenService keyCloakTokenService;
    private final RestTemplate restTemplate;
    private final UserRepo userRepo;

    public void registerAgent(UserRegistrationRequest request) {
        String adminToken = keyCloakTokenService.getUsersServiceAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", request.getEmail());
        userPayload.put("email", request.getEmail());
        userPayload.put("firstName", request.getFirstName());
        userPayload.put("lastName", request.getLastName());
        userPayload.put("enabled", true);
        userPayload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", request.getPassword(),
                "temporary", true
        )));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:8180/admin/realms/{realm}/users",
                entity,
                Void.class,
                "rent-payment"
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create user in Keycloak.");
        }

        // Fetch userId and assign role
        if (response.getStatusCode().is2xxSuccessful()) {
            String userId = fetchUserId(request.getEmail(), adminToken);

            if (userId == null) throw new RuntimeException("User created but ID not found.");

            assignRoleToUser(userId, "AGENT", adminToken);

            // ✅ Save to local DB
            UserEntity agent = UserEntity.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phoneNumber(request.getPhoneNumber())
                    .role(Role.AGENT)
                    .build();

            userRepo.save(agent);
        }

    }

    // 🔽 Add this helper method to fetch the created user's ID
    private String fetchUserId(String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:8180/admin/realms/{realm}/users?email={email}",
                HttpMethod.GET,
                entity,
                List.class,
                "rent-payment",
                email
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
            Map<String, Object> user = (Map<String, Object>) response.getBody().get(0);
            return (String) user.get("id");
        }

        return null;
    }

    // 🔽 Add this helper method to assign the agent role to the new user
    private void assignRoleToUser(String userId, String roleName, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Get realm roles to match exact role representation from Keycloak
        ResponseEntity<Map> rolesResponse = restTemplate.exchange(
                "http://localhost:8180/admin/realms/{realm}/roles/{roleName}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class,
                "rent-payment",
                roleName
        );

        Map<String, Object> role = (Map<String, Object>) rolesResponse.getBody();
        if (role == null) {
            throw new RuntimeException("Role not found in Keycloak: " + roleName);
        }

        HttpEntity<List<Map<String, Object>>> roleEntity = new HttpEntity<>(List.of(role), headers);

        restTemplate.postForEntity(
                "http://localhost:8180/admin/realms/{realm}/users/{userId}/role-mappings/realm",
                roleEntity,
                Void.class,
                "rent-payment",
                userId
        );
    }
}
