package com.example.demo.service;

import java.util.List;
import com.example.demo.model.HorarioDisponivel;

public interface HorarioDisponivelService {
        
    void save(HorarioDisponivel horarioDisponivel); 
    
    HorarioDisponivel findById(long id); 
    
    void deleteById(long id); 
    
    List<HorarioDisponivel> findTodosDisponiveis(); 

    List<HorarioDisponivel> findAll();
    
    List<HorarioDisponivel> findByMedicoId(Long medicoId); 
}