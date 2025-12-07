package com.example.demo.repository;

import com.example.demo.model.Consulta;
import com.example.demo.model.HorarioDisponivel;
import com.example.demo.model.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByHorarioId(Long horarioId);
    boolean existsByHorario(HorarioDisponivel horario);
    boolean existsByPacienteIdAndHorarioId(Long pacienteId, Long horarioId);
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByPaciente(Paciente paciente);
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.paciente.id = :pacienteId")
    long countByPacienteId(Long pacienteId);
    @Query("""
            SELECT c
            FROM Consulta c
            WHERE c.paciente.id = :pacienteId
              AND c.horario.dataHora > CURRENT_TIMESTAMP
            ORDER BY c.horario.dataHora ASC
            """)
    List<Consulta> findProximaConsulta(Long pacienteId);
    @Query("""
            SELECT COUNT(c) 
            FROM Consulta c 
            WHERE c.paciente.id = :pacienteId
                AND c.horario.dataHora < CURRENT_TIMESTAMP
            """)
    long countConsultasRealizadas(Long pacienteId);

}