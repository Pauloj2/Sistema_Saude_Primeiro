package com.example.demo.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.repository.HorarioDisponivelRepository;
import com.example.demo.service.HorarioDisponivelService;

@Service
public class HorarioDisponivelServiceImpl implements HorarioDisponivelService {

    // 🔑 Melhoria: Injeção de construtor em vez de @Autowired em campo
    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public HorarioDisponivelServiceImpl(HorarioDisponivelRepository horarioDisponivelRepository) {
        this.horarioDisponivelRepository = horarioDisponivelRepository;
    }

    // ----------------------------------------------------------------------
    // 🔑 CORREÇÕES DE PADRÃO DE NOMECLATURA (Para funcionar com Controllers)
    // ----------------------------------------------------------------------

    @Override
    public List<HorarioDisponivel> findAll() { // Substitui getAllHorarioDisponivel
        return horarioDisponivelRepository.findAll();
    }

    @Override
    public List<HorarioDisponivel> findTodosDisponiveis() { // Substitui getHorariosDisponiveis
        return horarioDisponivelRepository.findByDisponivelTrue();
    }

    @Override
    public List<HorarioDisponivel> findByMedicoId(Long medicoId) { // Substitui getHorariosByMedico
        return horarioDisponivelRepository.findByMedicoId(medicoId);
    }

    @Override
    public HorarioDisponivel findById(long id) { // Substitui getHorarioDisponivelById
        return horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id));
    }

    @Override
    public void deleteById(long id) { // Substitui deleteHorarioDisponivelById
        horarioDisponivelRepository.deleteById(id);
    }

    // ----------------------------------------------------------------------
    // 🔑 CORREÇÃO DO MÉTODO SAVE
    // ----------------------------------------------------------------------

    @Override
    public void save(HorarioDisponivel horario) { // Substitui saveHorarioDisponivel

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
}