package com.example.demo.controller;

import com.example.demo.model.Medicamento;
import com.example.demo.service.MedicamentoService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicamento") // ← MUDADO PARA SINGULAR
@PreAuthorize("hasRole('ATENDENTE')")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    // 🔥 CONSULTA para PACIENTE
    @GetMapping("/consulta")
    @PreAuthorize("hasAnyRole('PACIENTE','ATENDENTE')")
    public String consultaMedicamentos(
            @RequestParam(name = "q", required = false) String termo,
            Model model) {
        if (termo != null && !termo.isBlank()) {
            model.addAttribute("resultados", service.buscarPorNome(termo));
        } else {
            model.addAttribute("resultados", List.of());
        }
        model.addAttribute("query", termo);
        return "estoque/consulta";
    }

    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public String listar(Model model) {
        model.addAttribute("medicamentos", service.listarTodos());
        return "medicamento/listar";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String novo(Model model) {
        model.addAttribute("medicamento", new Medicamento());
        return "medicamento/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String salvar(@ModelAttribute Medicamento medicamento, RedirectAttributes ra) {
        try {
            service.salvar(medicamento);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento cadastrado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/medicamento"; // ← ATUALIZADO
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("medicamento", service.buscarPorId(id));
        return "medicamento/form";
    }

    @PostMapping("/atualizar/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String atualizar(@PathVariable Long id, @ModelAttribute Medicamento dados, RedirectAttributes ra) {
        try {
            service.atualizar(id, dados);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/medicamento"; // ← ATUALIZADO
    }

    @GetMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.deletar(id);
            ra.addFlashAttribute("mensagemSucesso", "Medicamento removido.");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/medicamento"; // ← ATUALIZADO
    }
}