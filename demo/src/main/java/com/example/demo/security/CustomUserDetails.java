package com.example.demo.security;

import com.example.demo.model.Paciente;
import com.example.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Optional<Paciente> paciente;

    public CustomUserDetails(User user, Optional<Paciente> paciente) {
        this.user = user;
        this.paciente = paciente == null ? Optional.empty() : paciente;
    }

    public Optional<Paciente> getPaciente() {
        return paciente;
    }

    public boolean hasRole(String role) {
        if (user.getRole() == null)
            return false;
        return user.getRole().equalsIgnoreCase(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Usuário sem ROLE definida: " + user.getEmail());
        }

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
