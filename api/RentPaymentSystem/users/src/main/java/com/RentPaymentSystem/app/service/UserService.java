package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.dto.UserRegistrationResponse;
import com.RentPaymentSystem.app.dto.UserValidationResponse;
import com.RentPaymentSystem.app.dto.UserValidationRequest;
import com.RentPaymentSystem.app.entity.UserEntity;
import com.RentPaymentSystem.app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserValidationResponse validatePassword(UserValidationRequest request) {
        log.info(String.format("Validating password: %s", request.getPassword()));

        // fetch user from the database
        UserEntity user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        log.info(user.toString());

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatch){
            throw new RuntimeException("Invalid password");
        }
        return new UserValidationResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().toString()
        );
    }

    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        // Check if email already exists
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        var user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepo.save(user);

        return UserRegistrationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    public List<UserEntity> findAll() {
        return userRepo.findAll();
    }
}
