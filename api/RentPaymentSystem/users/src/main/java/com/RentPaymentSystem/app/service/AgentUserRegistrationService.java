package com.RentPaymentSystem.app.service;


import com.RentPaymentSystem.app.dto.UserCreationResponse;
import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.Role;
import com.RentPaymentSystem.app.entity.UserEntity;
import com.RentPaymentSystem.app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentUserRegistrationService implements UserRegistrationService {
    private final KeycloakUserServiceImpl keycloakUserServiceImpl;
    private final UserRepo userRepo;


    @Override
    public void registerLandlord(UserRegistrationRequest request) {
        String Id = keycloakUserServiceImpl.createUserAndAssignRole(request, Role.LANDLORD);
        saveUserToDB(request, Role.LANDLORD);
    }

    @Override
    public void registerTenant(UserRegistrationRequest request) {
        String Id = keycloakUserServiceImpl.createUserAndAssignRole(request, Role.LANDLORD);
        saveUserToDB(request, Role.TENANT);
    }

    @Override
    public void registerCareTaker(UserRegistrationRequest request) {
        String Id = keycloakUserServiceImpl.createUserAndAssignRole(request, Role.LANDLORD);
        saveUserToDB(request, Role.CARETAKER);
    }

    public void saveUserToDB(UserRegistrationRequest request, Role role) {
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .build();

        userRepo.save(user);
    }
}
