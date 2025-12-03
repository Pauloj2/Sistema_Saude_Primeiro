package com.example.demo.service;

import java.util.List;
import com.example.demo.model.HorarioDisponivel;

public interface HorarioDisponivelService {

    List<HorarioDisponivel> getAllHorarioDisponivel();
    List<HorarioDisponivel> getHorariosDisponiveis();
    List<HorarioDisponivel> getHorariosByMedico(Long medicoId);
    void saveHorarioDisponivel(HorarioDisponivel horarioDisponivel);
    HorarioDisponivel getHorarioDisponivelById(long id);
    void deleteHorarioDisponivelById(long id);
}
