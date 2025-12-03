package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.service.MedicoService;
import com.example.demo.service.HorarioDisponivelService;

@Controller
public class HomeController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private HorarioDisponivelService horarioService;

    @GetMapping("/")
    public String home(Model model) {

        List<HorarioDisponivel> todosHorarios = horarioService.getAllHorarioDisponivel();

        long totalMedicos = medicoService.getAllMedico().size();

        long totalHorarios = todosHorarios.size();

        long horariosDisponiveis = todosHorarios.stream()
                .filter(HorarioDisponivel::isDisponivel)
                .count();

        long horariosAgendados = todosHorarios.stream()
                .filter(horario -> !horario.isDisponivel()) // ← MUDANÇA AQUI
                .count();

        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalHorarios", totalHorarios);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("horariosAgendados", horariosAgendados);

        return "dashboard";
    }
}