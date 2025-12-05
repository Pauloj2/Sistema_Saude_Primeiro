package com.example.demo.impl;

import com.example.demo.model.Diagnostico;
import com.example.demo.repository.DiagnosticoRepository;
import com.example.demo.service.DiagnosticoService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository repository;

    public DiagnosticoServiceImpl(DiagnosticoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Diagnostico salvar(Diagnostico d) {
        return repository.save(d);
    }

    @Override
    public List<Diagnostico> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Diagnostico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnóstico não encontrado"));
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
