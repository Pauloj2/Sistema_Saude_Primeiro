package com.example.demo.service;

import com.example.demo.model.Consulta;
import java.util.List;
import java.util.Optional;

public interface ConsultaService {

    List<Consulta> findAll();
    Optional<Consulta> findById(Long id);
    Consulta save(Consulta consulta);
    List<Consulta> findByPacienteId(Long pacienteId);
    void deleteById(Long id);
    boolean existsByHorarioId(Long horarioId);
}