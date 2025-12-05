package com.example.demo.repository;

import com.example.demo.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    boolean existsByCodigo(String codigo);
}
