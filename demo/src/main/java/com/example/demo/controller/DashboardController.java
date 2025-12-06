package com.example.demo.controller;

import com.example.demo.service.MedicoService;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.ConsultaService;
import com.example.demo.service.MedicamentoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MedicoService medicoService;
    private final HorarioDisponivelService horarioService;
    private final ConsultaService consultaService;
    private final MedicamentoService medicamentoService;

    public DashboardController(
            MedicoService medicoService,
            HorarioDisponivelService horarioService,
            ConsultaService consultaService,
            MedicamentoService medicamentoService) {
        this.medicoService = medicoService;
        this.horarioService = horarioService;
        this.consultaService = consultaService;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String dashboard(Model model) {

        long totalMedicos = medicoService.count();
        long totalHorarios = horarioService.count();
        long horariosDisponiveis = horarioService.countDisponiveis();
        long horariosAgendados = consultaService.count();
        long totalMedicamentos = medicamentoService.count();

        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalHorarios", totalHorarios);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("horariosAgendados", horariosAgendados);
        model.addAttribute("totalMedicamentos", totalMedicamentos);

        return "dashboard";
    }
}
