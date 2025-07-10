package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.*;
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
    private final KeycloakUserServiceImpl keycloakUserServiceImpl;
    private final UserRepo userRepo;


    public void registerAgent(UserRegistrationRequest request) {
        String agentId = keycloakUserServiceImpl.createUserAndAssignRole(request, Role.AGENT);

            //  Save to local DB
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



