package com.example.demo.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.repository.HorarioDisponivelRepository;
import com.example.demo.service.HorarioDisponivelService;

@Service
public class HorarioDisponivelServiceImpl implements HorarioDisponivelService {

    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public HorarioDisponivelServiceImpl(HorarioDisponivelRepository horarioDisponivelRepository) {
        this.horarioDisponivelRepository = horarioDisponivelRepository;
    }

    @Override
    public List<HorarioDisponivel> findAll() {
        return horarioDisponivelRepository.findAll();
    }

    @Override
    public List<HorarioDisponivel> findTodosDisponiveis() {
        return horarioDisponivelRepository.findByDisponivelTrue();
    }

    @Override
    public List<HorarioDisponivel> findByMedicoId(Long medicoId) {
        return horarioDisponivelRepository.findByMedicoId(medicoId);
    }

    @Override
    public HorarioDisponivel findById(long id) {
        return horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id));
    }

    @Override
    public void deleteById(long id) {
        horarioDisponivelRepository.deleteById(id);
    }

    @Override
    public void save(HorarioDisponivel horario) {

        if (horario.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é permitido cadastrar horários no passado.");
        }

        boolean existe = horarioDisponivelRepository
                .existsByMedicoIdAndDataHora(horario.getMedico().getId(), horario.getDataHora());

        if (existe) {
            throw new RuntimeException("Este horário já existe para este médico.");
        }

        horarioDisponivelRepository.save(horario);
    }

    @Override
    public long count() {
        return horarioDisponivelRepository.count();
    }

    @Override
    public long countDisponiveis() {
        return horarioDisponivelRepository.countByDisponivelTrue();
    }
}
