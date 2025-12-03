package com.example.demo.impl;

import com.example.demo.model.Consulta;
import com.example.demo.model.HorarioDisponivel;
import com.example.demo.repository.ConsultaRepository;
import com.example.demo.repository.HorarioDisponivelRepository;
import com.example.demo.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private HorarioDisponivelRepository horarioRepository;

    @Override
    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    @Override
    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    @Override
    @Transactional
    public Consulta save(Consulta consulta) {

        if (consulta.getPaciente() == null) {
            throw new IllegalArgumentException("Paciente deve ser selecionado.");
        }

        if (consulta.getHorario() == null) {
            throw new IllegalArgumentException("Horário não pode ser nulo.");
        }

        HorarioDisponivel horario = consulta.getHorario();

        if (!horario.isDisponivel()) {
            throw new IllegalStateException("Este horário já está ocupado.");
        }

        if (consultaRepository.existsByHorarioId(horario.getId())) {
            throw new IllegalStateException("Já existe consulta agendada para este horário.");
        }

        horario.setDisponivel(false);
        horarioRepository.save(horario);

        return consultaRepository.save(consulta);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);

        if (consultaOpt.isPresent()) {
            Consulta consulta = consultaOpt.get();

            HorarioDisponivel horario = consulta.getHorario();
            if (horario != null) {
                horario.setDisponivel(true);
                horarioRepository.save(horario);
            }
        }
        consultaRepository.deleteById(id);
    }

    @Override
    public boolean existsByHorarioId(Long horarioId) {
        return consultaRepository.existsByHorarioId(horarioId);
    }
}
