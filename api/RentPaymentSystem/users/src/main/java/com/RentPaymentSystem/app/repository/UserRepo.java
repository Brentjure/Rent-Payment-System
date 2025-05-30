package com.RentPaymentSystem.app.repository;

import com.RentPaymentSystem.app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    public Optional<UserEntity> findByEmail(String email);
}
