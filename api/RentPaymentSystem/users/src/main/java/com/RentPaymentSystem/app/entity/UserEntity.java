package com.RentPaymentSystem.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Column(name="id")
    int id;

    @JsonProperty("first_name")
    @Column(name="first_name")
    String firstName;

    @JsonProperty("last_name")
    @Column(name="last_name")
    String lastName;

    @JsonProperty("phone_number")
    @Column(name="phone_number")
    String phoneNumber;

    @JsonProperty("email")
    @Column(name="email")
    String email;

    @JsonProperty("password")
    @Column(name="password")
    String password;

    @JsonProperty("role")
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    Role role;

    @JsonProperty("last_login")
    @Column(name="last_login")
    LocalDateTime lastLogin;

    @JsonProperty("created_at")
    @Column(name="created_at")
    LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Column(name="updated_at")
    LocalDateTime updatedAt;

}
