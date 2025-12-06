package com.example.demo.impl;

import com.example.demo.model.Medicamento;
import com.example.demo.model.MovimentacaoEstoque;
import com.example.demo.model.TipoMovimentacao;
import com.example.demo.repository.MedicamentoRepository;
import com.example.demo.repository.MovimentacaoEstoqueRepository;
import com.example.demo.service.MovimentacaoEstoqueService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepo;
    private final MedicamentoRepository medicamentoRepo;

    public MovimentacaoEstoqueServiceImpl(
            MovimentacaoEstoqueRepository movimentacaoRepo,
            MedicamentoRepository medicamentoRepo) {

        this.movimentacaoRepo = movimentacaoRepo;
        this.medicamentoRepo = medicamentoRepo;
    }

    @Override
    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(
            Long medicamentoId,
            int quantidade,
            TipoMovimentacao tipo,
            String observacao) {

        Medicamento med = medicamentoRepo.findById(medicamentoId)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado."));

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero.");
        }

        // REGRA DE NEGÓCIO — impedir estoque negativo
        if (tipo == TipoMovimentacao.SAIDA && med.getEstoqueAtual() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para saída.");
        }

        // Atualiza o estoque
        switch (tipo) {
            case ENTRADA -> med.setEstoqueAtual(med.getEstoqueAtual() + quantidade);
            case SAIDA -> med.setEstoqueAtual(med.getEstoqueAtual() - quantidade);
            case AJUSTE -> med.setEstoqueAtual(quantidade); // Ajuste é redefinição
        }

        medicamentoRepo.save(med);

        // Registra a movimentação
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setMedicamento(med);
        mov.setQuantidade(quantidade);
        mov.setTipo(tipo);
        mov.setObservacao(observacao);

        return movimentacaoRepo.save(mov);
    }

    @Override
    public List<MovimentacaoEstoque> listarPorMedicamento(Long medicamentoId) {
        return movimentacaoRepo.findByMedicamentoId(medicamentoId);
    }

    @Override
    public List<MovimentacaoEstoque> listarTodas() {
        return movimentacaoRepo.findAll();
    }
}
