package com.example.demo.impl;

import com.example.demo.exception.AgendamentoException;
import com.example.demo.model.Consulta;
import com.example.demo.model.HorarioDisponivel;
import com.example.demo.model.Paciente;
import com.example.demo.model.StatusConsulta;
import com.example.demo.repository.ConsultaRepository;
import com.example.demo.repository.HorarioDisponivelRepository;
import com.example.demo.service.ConsultaService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final HorarioDisponivelRepository horarioRepository;

    public ConsultaServiceImpl(
            ConsultaRepository consultaRepository,
            HorarioDisponivelRepository horarioRepository) {
        this.consultaRepository = consultaRepository;
        this.horarioRepository = horarioRepository;
    }

    @Override
    @Transactional
    public Consulta agendar(Consulta consulta) throws AgendamentoException {

        if (consulta.getPaciente() == null) {
            throw new AgendamentoException("Paciente não informado.");
        }

        if (consulta.getHorario() == null) {
            throw new AgendamentoException("Horário não informado.");
        }

        HorarioDisponivel horario = consulta.getHorario();

        horarioRepository.findById(horario.getId())
                .orElseThrow(() -> new AgendamentoException("Horário inexistente."));

        if (!horario.isDisponivel()) {
            throw new AgendamentoException("O horário selecionado não está disponível.");
        }

        if (consultaRepository.existsByHorario(horario)) {
            throw new AgendamentoException("Já existe consulta marcada para esse horário.");
        }

        Consulta novaConsulta = consultaRepository.save(consulta);

        horario.setDisponivel(false);
        horarioRepository.save(horario);

        return novaConsulta;
    }

    @Override
    @Transactional
    public void cancelar(Long consultaId) throws AgendamentoException {

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new AgendamentoException("Consulta não encontrada."));

        LocalDateTime limite = consulta.getHorario().getDataHora().minusHours(24);

        if (LocalDateTime.now().isAfter(limite)) {
            throw new AgendamentoException("Cancelamento só é permitido com 24 horas de antecedência.");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);

        HorarioDisponivel horario = consulta.getHorario();
        horario.setDisponivel(true);
        horarioRepository.save(horario);
    }

    @Override
    public List<Consulta> buscarConsultasPorPaciente(Paciente paciente) {
        return consultaRepository.findByPaciente(paciente);
    }

    @Override
    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    @Override
    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    @Override
    @Transactional
    public Consulta save(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    @Override
    public long count() {
        return consultaRepository.count();
    }

    @Override
    public long countConsultasPorPaciente(Long pacienteId) {
        return consultaRepository.countByPacienteId(pacienteId);
    }

    @Override
    public Optional<Consulta> buscarProximaConsulta(Long pacienteId) {
        List<Consulta> result = consultaRepository.findProximaConsulta(pacienteId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public long countConsultasRealizadas(Long pacienteId) {
        return consultaRepository.countConsultasRealizadas(pacienteId);
    }

}
