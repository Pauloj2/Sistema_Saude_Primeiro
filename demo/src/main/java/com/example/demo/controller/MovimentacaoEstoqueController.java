package com.example.demo.controller;

import com.example.demo.model.TipoMovimentacao;
import com.example.demo.service.MedicamentoService;
import com.example.demo.service.MovimentacaoEstoqueService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoque")
@PreAuthorize("hasRole('ATENDENTE')")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService estoqueService;
    private final MedicamentoService medicamentoService;

    public MovimentacaoEstoqueController(
            MovimentacaoEstoqueService estoqueService,
            MedicamentoService medicamentoService) {

        this.estoqueService = estoqueService;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public String historico(Model model) {
        model.addAttribute("movimentacoes", estoqueService.listarTodas());
        return "estoque/historico";
    }

    @GetMapping("/entrada")
    public String entradaForm(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        return "estoque/entrada";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(
            @RequestParam Long medicamentoId,
            @RequestParam int quantidade,
            @RequestParam(required = false) String observacao,
            RedirectAttributes ra) {

        try {
            estoqueService.registrarMovimentacao(
                    medicamentoId, quantidade, TipoMovimentacao.ENTRADA, observacao);

            ra.addFlashAttribute("mensagemSucesso", "Entrada registrada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/estoque";
    }

    @GetMapping("/saida")
    public String saidaForm(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        return "estoque/saida";
    }

    @PostMapping("/saida")
    public String registrarSaida(
            @RequestParam Long medicamentoId,
            @RequestParam int quantidade,
            @RequestParam(required = false) String observacao,
            RedirectAttributes ra) {

        try {
            estoqueService.registrarMovimentacao(
                    medicamentoId, quantidade, TipoMovimentacao.SAIDA, observacao);

            ra.addFlashAttribute("mensagemSucesso", "Saída registrada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/estoque";
    }

    @GetMapping("/ajuste")
    public String ajusteForm(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        return "estoque/ajuste";
    }

    @PostMapping("/ajuste")
    public String registrarAjuste(
            @RequestParam Long medicamentoId,
            @RequestParam int quantidade,
            @RequestParam(required = false) String observacao,
            RedirectAttributes ra) {

        try {
            estoqueService.registrarMovimentacao(
                    medicamentoId, quantidade, TipoMovimentacao.AJUSTE, observacao);

            ra.addFlashAttribute("mensagemSucesso", "Ajuste realizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/estoque";
    }
}
