package com.example.demo.controller;

import com.example.demo.exception.AgendamentoException;
import com.example.demo.model.Consulta;
import com.example.demo.model.Paciente;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ConsultaService;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;
import com.example.demo.service.PacienteService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final HorarioDisponivelService horarioService;
    private final PacienteService pacienteService;

    public ConsultaController(
            ConsultaService consultaService,
            MedicoService medicoService,
            HorarioDisponivelService horarioService,
            PacienteService pacienteService) {
        this.consultaService = consultaService;
        this.medicoService = medicoService;
        this.horarioService = horarioService;
        this.pacienteService = pacienteService;
    }

    // ---------------------------------------------------------------------
    // 0. ATENDENTE — Listar Todas as Consultas
    // ---------------------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public String listarTodasConsultas(Model model) {
        model.addAttribute("consultasList", consultaService.findAll());
        return "consulta/lista";
    }

    // ---------------------------------------------------------------------
    // 1. PACIENTE — Ver Minhas Consultas
    // ---------------------------------------------------------------------
    @GetMapping("/minhas")
    @PreAuthorize("hasRole('PACIENTE')")
    public String minhasConsultas(Authentication auth, Model model) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        Paciente paciente = principal.getPaciente()
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado na sessão."));

        model.addAttribute("consultasList", consultaService.buscarConsultasPorPaciente(paciente));

        return "consulta/minhas";
    }

    // ---------------------------------------------------------------------
    // 2. FORMULÁRIO DE AGENDAMENTO
    // ---------------------------------------------------------------------
    @GetMapping("/agendar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String agendarForm(Authentication auth, Model model) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("medicos", medicoService.findAll());
        model.addAttribute("horariosDisponiveis", horarioService.findTodosDisponiveis());

        // Se for atendente, listamos todos os pacientes para selecionar no formulário
        if (principal.hasRole("ROLE_ATENDENTE")) {
            model.addAttribute("pacientes", pacienteService.findAll());
        }

        return "consulta/agendar";
    }

    // ---------------------------------------------------------------------
    // 3. PROCESSAR AGENDAMENTO
    // ---------------------------------------------------------------------
    @PostMapping("/agendar")
    @PreAuthorize("isAuthenticated()")
    public String agendar(
            @ModelAttribute("consulta") Consulta consulta,
            @RequestParam(value = "pacienteId", required = false) Long pacienteId,
            @RequestParam(value = "medicoId", required = true) Long medicoId,
            @RequestParam(value = "horarioId", required = true) Long horarioId,
            Authentication auth,
            RedirectAttributes attributes) {
        try {
            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

            // PACIENTE agendando para ele mesmo
            if (principal.hasRole("ROLE_PACIENTE")) {
                Paciente paciente = principal.getPaciente()
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));
                consulta.setPaciente(paciente);
            }

            // ATENDENTE agendando para alguém
            if (principal.hasRole("ROLE_ATENDENTE")) {
                if (pacienteId == null) {
                    attributes.addFlashAttribute("erro", "Selecione um paciente.");
                    return "redirect:/consultas/agendar";
                }

                Paciente paciente = pacienteService.findById(pacienteId);
                consulta.setPaciente(paciente);
            }

            // Define médico e horário
            consulta.setMedico(medicoService.findById(medicoId));
            consulta.setHorario(horarioService.findById(horarioId));

            consultaService.agendar(consulta);

            attributes.addFlashAttribute("sucesso", "Consulta agendada com sucesso!");

            return (principal.hasRole("ROLE_PACIENTE"))
                    ? "redirect:/consultas/minhas"
                    : "redirect:/consultas";

        } catch (AgendamentoException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/consultas/agendar";
        }
    }

    // ---------------------------------------------------------------------
    // 4. CANCELAMENTO
    // ---------------------------------------------------------------------
    @GetMapping("/cancelar/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String cancelar(
            @PathVariable Long id,
            Authentication auth,
            RedirectAttributes attributes) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        try {
            // PACIENTE só pode cancelar a própria consulta
            if (principal.hasRole("ROLE_PACIENTE")) {

                Paciente paciente = principal.getPaciente()
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

                Consulta consulta = consultaService.findById(id)
                        .orElseThrow(() -> new AgendamentoException("Consulta não encontrada."));

                if (!consulta.getPaciente().getId().equals(paciente.getId())) {
                    attributes.addFlashAttribute("erro", "Você não pode cancelar consultas de outro paciente.");
                    return "redirect:/consultas/minhas";
                }
            }

            consultaService.cancelar(id);

            attributes.addFlashAttribute("sucesso", "Consulta cancelada com sucesso!");

        } catch (AgendamentoException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }

        return principal.hasRole("ROLE_PACIENTE")
                ? "redirect:/consultas/minhas"
                : "redirect:/consultas";
    }
}