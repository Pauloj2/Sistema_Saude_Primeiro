package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Medico;
import com.example.demo.service.MedicoService;

@Controller
@RequestMapping("/medico") // Define base path para todas as rotas
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    // Página inicial - Lista todos os médicos
    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicosList", medicoService.getAllMedico()); // CORREÇÃO: getAllMedico() → getAllMedicos()
        return "medico/index"; // Mostra lista de médicos
    }

    // Mostrar formulário de cadastro
    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("medico", new Medico());
        return "medico/create";
    }

    // Processar formulário de cadastro/edição
    @PostMapping("/save")
    public String salvarMedico(@ModelAttribute("medico") Medico medico) {
        medicoService.saveMedico(medico);
        return "redirect:/medico"; // CORREÇÃO: Redireciona para lista de médicos
    }

    // Mostrar formulário de edição
    @GetMapping("/edit/{id}")
    public String editarMedico(@PathVariable Long id, Model model) {
        Medico medico = medicoService.getMedicoById(id);
        model.addAttribute("medico", medico);
        return "medico/edit";
    }

    // Excluir médico
    @GetMapping("/delete/{id}")
    public String excluirMedico(@PathVariable Long id) {
        medicoService.deleteMedicoById(id);
        return "redirect:/medico"; // CORREÇÃO: Redireciona para lista após excluir
    }
}