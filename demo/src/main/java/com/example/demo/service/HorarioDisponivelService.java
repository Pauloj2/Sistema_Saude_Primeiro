package com.example.demo.service;

import java.util.List;
import com.example.demo.model.HorarioDisponivel;

public interface HorarioDisponivelService {
    List<HorarioDisponivel> getAllHorarioDisponivel();
    void saveHorarioDisponivel(HorarioDisponivel horarioDisponivel);
    HorarioDisponivel getHorarioDisponivelById(long id);  
    void deleteHorarioDisponivelById(long id);
}
