package com.RentPaymentSystem.app.dto;

import lombok.Data;

@Data
public class KeycloakRoleRepresentation {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
}
