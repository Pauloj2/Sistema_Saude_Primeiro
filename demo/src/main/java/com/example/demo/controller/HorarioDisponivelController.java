package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/horarios")
public class HorarioDisponivelController {

    @Autowired
    private HorarioDisponivelService horarioService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("horariosList", horarioService.getAllHorarioDisponivel());
        return "horario/index";
    }

    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("horario", new HorarioDisponivel());
        model.addAttribute("medicos", medicoService.getAllMedico());
        return "horario/create";
    }

    @PostMapping("/save")
    public String salvarHorario(@Valid @ModelAttribute("horario") HorarioDisponivel horario,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("medicos", medicoService.getAllMedico());
            return "horario/create";
        }

        try {
            horarioService.saveHorarioDisponivel(horario);
        } catch (RuntimeException e) {
            model.addAttribute("medicos", medicoService.getAllMedico());
            model.addAttribute("erro", e.getMessage());
            return "horario/create";
        }

        return "redirect:/horarios";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {

        HorarioDisponivel horario = horarioService.getHorarioDisponivelById(id);

        model.addAttribute("horario", horario);
        model.addAttribute("medicos", medicoService.getAllMedico());
        return "horario/edit";
    }

    @PostMapping("/update/{id}")
    public String atualizarHorario(@PathVariable Long id,
                                   @Valid @ModelAttribute("horario") HorarioDisponivel horario,
                                   BindingResult result,
                                   Model model) {

        if (result.hasErrors()) {
            model.addAttribute("medicos", medicoService.getAllMedico());
            return "horario/edit";
        }

        try {
            horario.setId(id);
            horarioService.saveHorarioDisponivel(horario);
        } catch (RuntimeException e) {
            model.addAttribute("medicos", medicoService.getAllMedico());
            model.addAttribute("erro", e.getMessage());
            return "horario/edit";
        }

        return "redirect:/horarios";
    }

    @GetMapping("/delete/{id}")
    public String excluirHorario(@PathVariable Long id) {
        horarioService.deleteHorarioDisponivelById(id);
        return "redirect:/horarios";
    }

    @GetMapping("/disponiveis")
    public String listarHorariosDisponiveis(Model model) {
        model.addAttribute("horariosList", horarioService.getHorariosDisponiveis());
        return "horario/disponiveis";
    }

    @GetMapping("/medico/{id}")
    public String listarPorMedico(@PathVariable Long id, Model model) {
        model.addAttribute("horariosList", horarioService.getHorariosByMedico(id));
        model.addAttribute("medico", medicoService.getMedicoById(id));
        return "horario/por-medico";
    }
}
