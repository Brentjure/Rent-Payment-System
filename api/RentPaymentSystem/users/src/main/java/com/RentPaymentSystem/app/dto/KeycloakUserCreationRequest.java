package com.RentPaymentSystem.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class KeycloakUserCreationRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled = true;
    private List<Credentials> credentials;

    @Data
    public static class Credentials {
        private String type = "password";
        private String value;
        private boolean temporary = true;
    }
}
