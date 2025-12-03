package com.example.demo.service;

import com.example.demo.model.Paciente;
import java.util.List;

public interface PacienteService {

    Paciente save(Paciente paciente);
    List<Paciente> findAll();
    Paciente findById(Long id);
    void deleteById(Long id);
    boolean emailExiste(String email);
    boolean cpfExiste(String cpf);
}
