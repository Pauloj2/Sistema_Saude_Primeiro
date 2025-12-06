package com.example.demo.service;

import com.example.demo.model.Medicamento;

import java.util.List;

public interface MedicamentoService {

    Medicamento salvar(Medicamento medicamento);
    Medicamento atualizar(Long id, Medicamento dados);
    Medicamento buscarPorId(Long id);
    List<Medicamento> listarTodos();
    void deletar(Long id);
    boolean existePorNome(String nome);
    long count();
    List<Medicamento> buscarPorNome(String nome);
}
