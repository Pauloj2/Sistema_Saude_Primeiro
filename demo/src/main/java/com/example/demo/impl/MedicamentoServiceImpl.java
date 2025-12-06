package com.example.demo.impl;

import com.example.demo.model.Medicamento;
import com.example.demo.repository.MedicamentoRepository;
import com.example.demo.service.MedicamentoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository repository;

    public MedicamentoServiceImpl(MedicamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Medicamento salvar(Medicamento medicamento) {

        if (repository.existsByNome(medicamento.getNome())) {
            throw new RuntimeException("Já existe um medicamento com esse nome.");
        }

        return repository.save(medicamento);
    }

    @Override
    @Transactional
    public Medicamento atualizar(Long id, Medicamento dados) {
        Medicamento existente = buscarPorId(id);

        existente.setNome(dados.getNome());
        existente.setUnidade(dados.getUnidade());

        return repository.save(existente);
    }

    @Override
    public Medicamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado."));
    } 

    @Override
    public List<Medicamento> listarTodos() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNome(nome);
    }
}
