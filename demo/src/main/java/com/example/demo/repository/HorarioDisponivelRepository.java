package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.HorarioDisponivel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {

    List<HorarioDisponivel> findByDisponivelTrue();
    List<HorarioDisponivel> findByMedicoId(Long medicoId);
    boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);
}
