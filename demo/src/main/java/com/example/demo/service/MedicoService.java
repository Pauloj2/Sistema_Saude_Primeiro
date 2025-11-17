package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Medico;

public interface MedicoService {
    List <Medico> getAllMedico();
    void saveMedico(Medico medico);
    Medico getMedicoById(long id);
    void deleteMedicoById(long id);
    
}