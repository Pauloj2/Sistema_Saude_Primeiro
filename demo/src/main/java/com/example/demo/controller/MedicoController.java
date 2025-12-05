package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import com.example.demo.model.Medico;
import com.example.demo.service.MedicoService;

@Controller
@RequestMapping("/medico")
@PreAuthorize("hasRole('ATENDENTE')") 
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicosList", medicoService.getAllMedicos());
        return "medico/index";
    }

    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("medico", new Medico());
        return "medico/create";
    }

    @PostMapping("/save")
    public String salvarMedico(
            @ModelAttribute("medico") @Valid Medico medico,
            BindingResult result,
            RedirectAttributes attributes) {

        // Validação de erros de binding
        if (result.hasErrors()) {
            return "medico/create";
        }

        // Validação de CRM duplicado (apenas para novos médicos)
        if (medico.getId() == null && medicoService.existsByCrm(medico.getCrm())) {
            result.rejectValue("crm", "error.medico", "CRM já cadastrado no sistema");
            return "medico/create";
        }

        medicoService.saveMedico(medico);
        attributes.addFlashAttribute("sucesso", "Médico salvo com sucesso!");
        return "redirect:/medico";
    }

    @GetMapping("/edit/{id}")
    public String editarMedico(@PathVariable Long id, Model model) {
        Medico medico = medicoService.getMedicoById(id);
        model.addAttribute("medico", medico);
        return "medico/edit";
    }

    @PostMapping("/update")
    public String atualizarMedico(
            @ModelAttribute("medico") @Valid Medico medico,
            BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "medico/edit";
        }

        medicoService.saveMedico(medico);
        attributes.addFlashAttribute("sucesso", "Médico atualizado com sucesso!");
        return "redirect:/medico";
    }

    @GetMapping("/delete/{id}")
    public String excluirMedico(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            medicoService.deleteMedicoById(id);
            attributes.addFlashAttribute("sucesso", "Médico excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro",
                    "Não foi possível excluir o médico. Verifique se há consultas associadas.");
        }
        return "redirect:/medico";
    }
}