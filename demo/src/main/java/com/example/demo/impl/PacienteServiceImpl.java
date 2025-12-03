package com.example.demo.impl;

import com.example.demo.model.Paciente;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Paciente save(Paciente paciente) {

        if (paciente.getId() == null) {
            paciente.setSenha(passwordEncoder.encode(paciente.getSenha()));
        }

        return pacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente findById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    @Override
    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public boolean emailExiste(String email) {
        return pacienteRepository.existsByEmail(email);
    }

    @Override
    public boolean cpfExiste(String cpf) {
        return pacienteRepository.existsByCpf(cpf);
    }
}
