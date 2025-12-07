package com.example.demo.controller;

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

    private final HorarioDisponivelService horarioService;
    private final MedicoService medicoService;

    public HorarioDisponivelController(HorarioDisponivelService horarioService, MedicoService medicoService) {
        this.horarioService = horarioService;
        this.medicoService = medicoService;
    }

    @GetMapping
    public String listarHorarios(Model model) {
        // 🔑 CORREÇÃO: Usando findAll()
        model.addAttribute("horariosList", horarioService.findAll());
        return "horario/index";
    }

    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("horario", new HorarioDisponivel());
        // 🔑 CORREÇÃO: Usando findAll()
        model.addAttribute("medicos", medicoService.findAll());
        return "horario/create";
    }

    @PostMapping("/save")
    public String salvarHorario(@Valid @ModelAttribute("horario") HorarioDisponivel horario,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            // 🔑 CORREÇÃO: Usando findAll()
            model.addAttribute("medicos", medicoService.getAllMedicos());
            return "horario/create";
        }

        try {
            // 🔑 CORREÇÃO: Usando save()
            horarioService.save(horario);
        } catch (RuntimeException e) {
            // 🔑 CORREÇÃO: Usando findAll()
            model.addAttribute("medicos", medicoService.findAll());
            model.addAttribute("erro", e.getMessage());
            return "horario/create";
        }

        return "redirect:/horarios";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {

        // 🔑 CORREÇÃO: Usando findById()
        HorarioDisponivel horario = horarioService.findById(id);

        model.addAttribute("horario", horario);
        // 🔑 CORREÇÃO: Usando findAll()
        model.addAttribute("medicos", medicoService.findAll());
        return "horario/edit";
    }

    @PostMapping("/update/{id}")
    public String atualizarHorario(@PathVariable Long id,
            @Valid @ModelAttribute("horario") HorarioDisponivel horario,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            // 🔑 CORREÇÃO: Usando findAll()
            model.addAttribute("medicos", medicoService.findAll());
            return "horario/edit";
        }

        try {
            horario.setId(id);
            // 🔑 CORREÇÃO: Usando save()
            horarioService.save(horario);
        } catch (RuntimeException e) {
            // 🔑 CORREÇÃO: Usando findAll()
            model.addAttribute("medicos", medicoService.findAll());
            model.addAttribute("erro", e.getMessage());
            return "horario/edit";
        }

        return "redirect:/horarios";
    }

    @GetMapping("/delete/{id}")
    public String excluirHorario(@PathVariable Long id) {
        // 🔑 CORREÇÃO: Usando deleteById()
        horarioService.deleteById(id);
        return "redirect:/horarios";
    }

    @GetMapping("/disponiveis")
    public String listarHorariosDisponiveis(Model model) {
        // 🔑 CORREÇÃO: Usando findTodosDisponiveis() (método customizado correto)
        model.addAttribute("horariosList", horarioService.findTodosDisponiveis());
        return "horario/disponiveis";
    }

    @GetMapping("/medico/{id}")
    public String listarPorMedico(@PathVariable Long id, Model model) {
        // NOTA: Presume-se que o MedicoService tem um método getMedicoById(id)
        model.addAttribute("horariosList", horarioService.findByMedicoId(id));
        model.addAttribute("medico", medicoService.findById(id)); // 🔑 CORREÇÃO: Usando findById
        return "horario/por-medico";
    }
}