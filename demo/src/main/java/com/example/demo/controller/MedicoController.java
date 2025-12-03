package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

import com.example.demo.model.Medico;
import com.example.demo.service.MedicoService;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicosList", medicoService.getAllMedico());
        return "medico/index";
    }

    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("medico", new Medico());
        return "medico/create";
    }

    @PostMapping("/save")
    public String salvarMedico(@ModelAttribute("medico") @Valid Medico medico) {
        medicoService.saveMedico(medico);
        return "redirect:/medico";
    }

    @GetMapping("/edit/{id}")
    public String editarMedico(@PathVariable Long id, Model model) {
        Medico medico = medicoService.getMedicoById(id);
        model.addAttribute("medico", medico);
        return "medico/edit";
    }

    @GetMapping("/delete/{id}")
    public String excluirMedico(@PathVariable Long id) {
        medicoService.deleteMedicoById(id);
        return "redirect:/medico";
    }
}
