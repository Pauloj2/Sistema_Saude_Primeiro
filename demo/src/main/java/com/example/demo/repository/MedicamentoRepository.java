package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Medicamento;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    boolean existsByNome(String nome);
}
