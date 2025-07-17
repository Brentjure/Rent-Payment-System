package com.RentPaymentSystem.app.service;

import com.RentPaymentSystem.app.dto.UserCreationResponse;
import com.RentPaymentSystem.app.dto.UserRegistrationRequest;
import com.RentPaymentSystem.app.entity.UserEntity;

public interface UserRegistrationService {
    void registerLandlord(UserRegistrationRequest request);
    void registerTenant(UserRegistrationRequest request);
    void registerCareTaker(UserRegistrationRequest request);
}
