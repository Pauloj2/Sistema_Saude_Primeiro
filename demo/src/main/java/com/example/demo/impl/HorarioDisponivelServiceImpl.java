package com.example.demo.impl;

import java.util.List;
import java.util.Optional;

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
    public void saveHorarioDisponivel(HorarioDisponivel horarioDisponivel) { 
        this.horarioDisponivelRepository.save(horarioDisponivel); 
    }

    @Override
    public HorarioDisponivel getHorarioDisponivelById(long id) { 
        Optional<HorarioDisponivel> optional = horarioDisponivelRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Horário não encontrado com id: " + id);
        }
    }

    @Override
    public void deleteHorarioDisponivelById(long id) {
        this.horarioDisponivelRepository.deleteById(id);
    }
}