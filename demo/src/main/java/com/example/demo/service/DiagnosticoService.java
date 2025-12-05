package com.example.demo.service;

import com.example.demo.model.Diagnostico;

import java.util.List;

public interface DiagnosticoService {

    Diagnostico salvar(Diagnostico d);
    List<Diagnostico> listarTodos();
    Diagnostico buscarPorId(Long id);
    void deletar(Long id);
    List<Diagnostico> findByIds(List<Long> ids);
}
