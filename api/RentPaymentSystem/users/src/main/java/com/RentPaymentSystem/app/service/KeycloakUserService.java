package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.Role;

public interface KeycloakUserService {
    String createUserAndAssignRole(UserRegistrationRequest request, Role role);
}
