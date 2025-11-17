package com.example.demo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Medico;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.service.MedicoService;

@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepository medicoRepository; 

    @Override
    public List<Medico> getAllMedico() { 
        return medicoRepository.findAll();
    }

    @Override
    public void saveMedico(Medico medico) { 
        this.medicoRepository.save(medico);
    }

    @Override
    public Medico getMedicoById(long id) { 
        Optional<Medico> optional = medicoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Médico não encontrado com id: " + id); 
        }
    }

    @Override
    public void deleteMedicoById(long id) { 
        this.medicoRepository.deleteById(id);
    }
}