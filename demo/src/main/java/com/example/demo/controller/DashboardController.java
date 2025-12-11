package com.example.demo.controller;

import com.example.demo.model.Consulta;
import com.example.demo.model.Paciente;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ConsultaService;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;
import com.example.demo.service.MedicamentoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    public String dashboard(Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        model.addAttribute("totalMedicos", medicoService.count());
        model.addAttribute("totalHorarios", horarioService.count());
        model.addAttribute("horariosDisponiveis", horarioService.countDisponiveis());
        model.addAttribute("horariosAgendados", consultaService.count());
        model.addAttribute("totalMedicamentos", medicamentoService.count());
        model.addAttribute("medicos", medicoService.getAllMedicos());

        if (userDetails.hasRole("ROLE_PACIENTE")) {

            Paciente paciente = userDetails.getPaciente()
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

            Long pacienteId = paciente.getId();

            long totalConsultasPaciente = consultaService.countConsultasPorPaciente(pacienteId);
            model.addAttribute("totalConsultasPaciente", totalConsultasPaciente);

            long consultasRealizadas = consultaService.countConsultasRealizadas(pacienteId);
            model.addAttribute("consultasRealizadas", consultasRealizadas);

            Optional<Consulta> proximaOpt = consultaService.buscarProximaConsulta(pacienteId);

            if (proximaOpt.isPresent()) {
                Consulta proxima = proximaOpt.get();

                long diasRestantes = ChronoUnit.DAYS.between(
                        LocalDateTime.now(),
                        proxima.getHorario().getDataHora());

                if (diasRestantes < 0)
                    diasRestantes = 0;

                model.addAttribute("temProximaConsulta", true);
                model.addAttribute("diasRestantes", diasRestantes);
            } else {
                model.addAttribute("temProximaConsulta", false);
            }

            model.addAttribute(
                    "consultasPaciente",
                    consultaService.buscarConsultasPorPaciente(paciente));
        }

        return "dashboard";
    }
}