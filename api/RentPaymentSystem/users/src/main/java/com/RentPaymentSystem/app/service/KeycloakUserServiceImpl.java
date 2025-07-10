package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.KeycloakRoleRepresentation;
import com.RentPaymentSystem.app.dto.KeycloakUserCreationRequest;
import com.RentPaymentSystem.app.dto.KeycloakUserRepresentation;
import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.Role;
import com.RentPaymentSystem.app.oauth2.KeyCloakTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final KeyCloakTokenService keyCloakTokenService;
    private final RestTemplate restTemplate;
    private static final String REALM = "rent-payment";
    private static final String KEYCLOAK_BASE_URL = "http://localhost:8180";
    String userCreationUrl = KEYCLOAK_BASE_URL + "/admin/realms/" + REALM + "/users";

    public String createUserAndAssignRole(UserRegistrationRequest request, Role role){
        String userServiceToken = keyCloakTokenService.getUsersServiceAccessToken();

        KeycloakUserCreationRequest userPayload = getKeycloakUserCreationRequest(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userServiceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<KeycloakUserCreationRequest> entity = new HttpEntity<>(userPayload, headers);

        log.info("Creating user {} in Keycloak", request.getEmail());

        ResponseEntity<Void> response = restTemplate.postForEntity(
                userCreationUrl,
                entity,
                Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create user in Keycloak.");
        }

        // Fetch userId and assign role
        if (response.getStatusCode().is2xxSuccessful()){
            String userId = fetchUserId(request.getEmail(), userServiceToken);

            if (userId == null) throw new RuntimeException("User created but ID not found.");

            assignRoleToUser(userId, role.name(), userServiceToken);
        }

        return null;
    }

    private static KeycloakUserCreationRequest getKeycloakUserCreationRequest(UserRegistrationRequest request) {
        KeycloakUserCreationRequest userPayload = new KeycloakUserCreationRequest();
        userPayload.setFirstName(request.getFirstName());
        userPayload.setLastName(request.getLastName());
        userPayload.setUsername(request.getEmail());
        userPayload.setEmail(request.getEmail());

        KeycloakUserCreationRequest.Credentials credentials = new KeycloakUserCreationRequest.Credentials();
        credentials.setValue(request.getPassword());
        userPayload.setCredentials(List.of(credentials));
        return userPayload;
    }

    //   helper method to fetch the created user's ID
    private String fetchUserId(String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserRepresentation[]> response = restTemplate.exchange(
                "http://localhost:8180/admin/realms/{realm}/users?email={email}",
                HttpMethod.GET,
                entity,
                KeycloakUserRepresentation[].class,
                "rent-payment",
                email
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0].getId();
        }
        return null;
    }

    // helper method to assign the agent role to the new user
    private void assignRoleToUser(String userId, String roleName, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Get realm roles to match exact role representation from Keycloak
        ResponseEntity<KeycloakRoleRepresentation> rolesResponse = restTemplate.exchange(
                "http://localhost:8180/admin/realms/{realm}/roles/{roleName}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KeycloakRoleRepresentation.class,
                "rent-payment",
                roleName
        );

        KeycloakRoleRepresentation role = rolesResponse.getBody();
        if (role == null) {
            throw new RuntimeException("Role not found in Keycloak: " + roleName);
        }

        HttpEntity<List<KeycloakRoleRepresentation>> roleEntity = new HttpEntity<>(List.of(role), headers);

        restTemplate.postForEntity(
                "http://localhost:8180/admin/realms/{realm}/users/{userId}/role-mappings/realm",
                roleEntity,
                Void.class,
                "rent-payment",
                userId
        );
    }
}
