package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Paciente;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    Optional<Paciente> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    
}