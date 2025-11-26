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

        // 1. Buscar todos os horários cadastrados no banco
        List<HorarioDisponivel> todosHorarios = horarioService.getAllHorarioDisponivel();

        // Aqui Calcula o total de médicos cadastrados
        long totalMedicos = medicoService.getAllMedico().size();

        // Total de horários cadastrados (disponíveis + agendados)
        long totalHorarios = todosHorarios.size();

        // Filtra apenas os horários que ainda estão disponíveis
        long horariosDisponiveis = todosHorarios.stream()
                .filter(HorarioDisponivel::isDisponivel)
                .count();

        // Filtra apenas os horários que já foram agendados
        long horariosAgendados = todosHorarios.stream()
                .filter(HorarioDisponivel::isAgendado)
                .count();

        // 3. Enviar dados para o template Thymeleaf -----------------------------

        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalHorarios", totalHorarios);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("horariosAgendados", horariosAgendados);

        // 4. Retorna a página dashboard.html
        return "dashboard";
    }
}
