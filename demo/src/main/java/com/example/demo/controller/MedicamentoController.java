package com.example.demo.controller;

import com.example.demo.model.Medicamento;
import com.example.demo.service.MedicamentoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicamentos")
@PreAuthorize("hasRole('ATENDENTE')")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicamentos", service.listarTodos());
        return "medicamento/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("medicamento", new Medicamento());
        return "medicamento/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Medicamento medicamento,
            RedirectAttributes ra) {

        try {
            service.salvar(medicamento);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento cadastrado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/medicamentos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Medicamento medicamento = service.buscarPorId(id);
        model.addAttribute("medicamento", medicamento);

        return "medicamento/form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Medicamento dados,
            RedirectAttributes ra) {

        try {
            service.atualizar(id, dados);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/medicamentos";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {

        try {
            service.deletar(id);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento removido.");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/medicamentos";
    }
}
