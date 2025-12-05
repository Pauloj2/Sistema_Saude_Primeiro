package com.example.demo.controller;

import com.example.demo.model.Diagnostico;
import com.example.demo.service.DiagnosticoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/diagnosticos")
@PreAuthorize("hasRole('ATENDENTE') or hasRole('ADMIN')")
public class DiagnosticoController {

    private final DiagnosticoService service;

    public DiagnosticoController(DiagnosticoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lista", service.listarTodos());
        return "diagnostico/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("diagnostico", new Diagnostico());
        return "diagnostico/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Diagnostico diagnostico,
            RedirectAttributes ra) {

        service.salvar(diagnostico);

        ra.addFlashAttribute("mensagemSucesso",
                "Diagnóstico salvo com sucesso!");

        return "redirect:/diagnosticos";
    }

    @GetMapping("/delete/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {

        service.deletar(id);

        ra.addFlashAttribute("mensagemSucesso",
                "Diagnóstico removido com sucesso!");

        return "redirect:/diagnosticos";
    }
}
