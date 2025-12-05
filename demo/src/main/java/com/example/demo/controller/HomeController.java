package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.service.MedicoService;
import com.example.demo.service.HorarioDisponivelService;

@Controller
public class HomeController {

    private final MedicoService medicoService;
    private final HorarioDisponivelService horarioService;

    public HomeController(MedicoService medicoService, HorarioDisponivelService horarioService) {
        this.medicoService = medicoService;
        this.horarioService = horarioService;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<HorarioDisponivel> todosHorarios = horarioService.findAll();

        long totalMedicos = medicoService.findAll().size();

        long totalHorarios = todosHorarios.size();

        long horariosDisponiveis = todosHorarios.stream()
                .filter(HorarioDisponivel::isDisponivel)
                .count();

        long horariosAgendados = todosHorarios.stream()
                .filter(horario -> !horario.isDisponivel())
                .count();

        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalHorarios", totalHorarios);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("horariosAgendados", horariosAgendados);

        return "dashboard";
    }
}