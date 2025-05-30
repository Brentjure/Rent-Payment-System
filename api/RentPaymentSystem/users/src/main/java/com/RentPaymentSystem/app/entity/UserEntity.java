package com.RentPaymentSystem.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
