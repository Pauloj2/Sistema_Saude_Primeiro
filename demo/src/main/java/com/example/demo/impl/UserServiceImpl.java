package com.example.demo.impl;

import com.example.demo.model.User;
import com.example.demo.model.Paciente;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;
    private final PacienteRepository pacienteRepo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepo,
            PacienteRepository pacienteRepo,
            @Lazy PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.pacienteRepo = pacienteRepo;
        this.encoder = encoder;
    }

    @Override
    public User save(User user) {

        if (user.getRole() != null && !user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole().toUpperCase());
        }

        // Evita recriptografar senha já encriptada
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        return userRepo.save(user);
    }

    @Override
    public Optional<User> findByEmailOptional(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean emailExiste(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado: " + email));

        if (user.getRole() == null || user.getRole().isBlank()) {
            throw new UsernameNotFoundException("Usuário sem ROLE válida: " + email);
        }

        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
            user.setRole(role);
            userRepo.save(user); 
        }

        Optional<Paciente> paciente = Optional.empty();

        if (role.equals("ROLE_PACIENTE")) {
            paciente = pacienteRepo.findByUser(user);
        }

        return new CustomUserDetails(user, paciente);
    }
}
