package com.example.demo.service;

import com.example.demo.model.MovimentacaoEstoque;
import com.example.demo.model.TipoMovimentacao;

import java.util.List;

public interface MovimentacaoEstoqueService {

    MovimentacaoEstoque registrarMovimentacao(
            Long medicamentoId,
            int quantidade,
            TipoMovimentacao tipo,
            String observacao);

    List<MovimentacaoEstoque> listarPorMedicamento(Long medicamentoId);

    List<MovimentacaoEstoque> listarTodas();
}
