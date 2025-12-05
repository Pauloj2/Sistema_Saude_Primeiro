package com.example.demo.service;

import com.example.demo.model.Consulta;
import com.example.demo.model.Paciente;
import com.example.demo.exception.AgendamentoException;

import java.util.List;
import java.util.Optional;

public interface ConsultaService {

    Consulta agendar(Consulta consulta) throws AgendamentoException;
    void cancelar(Long consultaId) throws AgendamentoException;
    List<Consulta> buscarConsultasPorPaciente(Paciente paciente);
    Optional<Consulta> findById(Long id);
    List<Consulta> findAll();
    Consulta save(Consulta consulta);
}
