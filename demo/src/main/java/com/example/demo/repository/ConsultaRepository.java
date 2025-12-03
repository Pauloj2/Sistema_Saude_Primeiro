package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByHorarioId(Long horarioId);
}