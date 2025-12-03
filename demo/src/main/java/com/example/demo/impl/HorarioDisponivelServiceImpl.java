package com.example.demo.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.repository.HorarioDisponivelRepository;
import com.example.demo.service.HorarioDisponivelService;

@Service
public class HorarioDisponivelServiceImpl implements HorarioDisponivelService {

    @Autowired
    private HorarioDisponivelRepository horarioDisponivelRepository;

    @Override
    public List<HorarioDisponivel> getAllHorarioDisponivel() {
        return horarioDisponivelRepository.findAll();
    }

    @Override
    public List<HorarioDisponivel> getHorariosDisponiveis() {
        return horarioDisponivelRepository.findByDisponivelTrue();
    }

    @Override
    public List<HorarioDisponivel> getHorariosByMedico(Long medicoId) {
        return horarioDisponivelRepository.findByMedicoId(medicoId);
    }

    @Override
    public void saveHorarioDisponivel(HorarioDisponivel horario) {

        // VALIDAÇÃO: impedir horário em data passada
        if (horario.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é permitido cadastrar horários no passado.");
        }

        // VALIDAÇÃO: impedir horários duplicados do mesmo médico
        boolean existe = horarioDisponivelRepository
                .existsByMedicoIdAndDataHora(horario.getMedico().getId(), horario.getDataHora());

        if (existe) {
            throw new RuntimeException("Este horário já existe para este médico.");
        }

        horarioDisponivelRepository.save(horario);
    }

    @Override
    public HorarioDisponivel getHorarioDisponivelById(long id) {
        return horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id));
    }

    @Override
    public void deleteHorarioDisponivelById(long id) {
        horarioDisponivelRepository.deleteById(id);
    }
}
