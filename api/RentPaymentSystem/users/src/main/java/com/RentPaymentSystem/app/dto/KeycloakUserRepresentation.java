package com.RentPaymentSystem.app.dto;

import lombok.Data;

@Data
public class KeycloakUserRepresentation {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
}
