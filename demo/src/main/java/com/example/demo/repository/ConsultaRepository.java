package com.example.demo.repository;

import com.example.demo.model.Consulta;
import com.example.demo.model.HorarioDisponivel;
import com.example.demo.model.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByHorarioId(Long horarioId);

    boolean existsByHorario(HorarioDisponivel horario);

    boolean existsByPacienteIdAndHorarioId(Long pacienteId, Long horarioId);

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByPaciente(Paciente paciente);
}