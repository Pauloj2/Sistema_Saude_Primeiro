package com.example.demo.controller;

import com.example.demo.exception.AgendamentoException;
import com.example.demo.model.Consulta;
import com.example.demo.model.Paciente;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.ConsultaService;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;
import com.example.demo.service.PacienteService;
import com.example.demo.service.DiagnosticoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final HorarioDisponivelService horarioService;
    private final PacienteService pacienteService;
    private final DiagnosticoService diagnosticoService;

    public ConsultaController(
            ConsultaService consultaService,
            MedicoService medicoService,
            HorarioDisponivelService horarioService,
            PacienteService pacienteService,
            DiagnosticoService diagnosticoService) {

        this.consultaService = consultaService;
        this.medicoService = medicoService;
        this.horarioService = horarioService;
        this.pacienteService = pacienteService;
        this.diagnosticoService = diagnosticoService;
    }

    // LISTAR TODAS (ATENDENTE)
    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public String listarTodasConsultas(Model model) {
        model.addAttribute("consultasList", consultaService.findAll());
        return "consulta/listar-consultas";
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasRole('PACIENTE')")
    public String minhasConsultas(Authentication auth, Model model) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        Paciente paciente = principal.getPaciente()
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado na sessão."));

        model.addAttribute("consultasList", consultaService.buscarConsultasPorPaciente(paciente));

        return "consulta/minhas-consultas";
    }

    @GetMapping("/agendar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String agendarForm(Authentication auth, Model model) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("medicos", medicoService.findAll());
        model.addAttribute("horarios", horarioService.findTodosDisponiveis());

        if (principal.hasRole("ROLE_ATENDENTE")) {
            model.addAttribute("pacientes", pacienteService.findAll());
        }

        return "consulta/agendar-consulta";
    }

    @PostMapping("/agendar")
    @PreAuthorize("isAuthenticated()")
    public String agendar(
            @ModelAttribute("consulta") Consulta consulta,
            @RequestParam(value = "pacienteId", required = false) Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam Long horarioId,
            Authentication auth,
            RedirectAttributes attributes) {

        try {
            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

            if (principal.hasRole("ROLE_PACIENTE")) {
                Paciente paciente = principal.getPaciente()
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));
                consulta.setPaciente(paciente);
            }

            if (principal.hasRole("ROLE_ATENDENTE")) {
                if (pacienteId == null) {
                    attributes.addFlashAttribute("mensagemErro", "Selecione um paciente.");
                    return "redirect:/consultas/agendar";
                }

                Paciente paciente = pacienteService.findById(pacienteId);
                consulta.setPaciente(paciente);
            }

            consulta.setMedico(medicoService.findById(medicoId));
            consulta.setHorario(horarioService.findById(horarioId));

            consultaService.agendar(consulta);

            attributes.addFlashAttribute("mensagemSucesso", "Consulta agendada com sucesso!");

            return principal.hasRole("ROLE_PACIENTE")
                    ? "redirect:/consultas/minhas"
                    : "redirect:/consultas";

        } catch (AgendamentoException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/consultas/agendar";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String editarConsulta(@PathVariable Long id, Model model) {

        Consulta consulta = consultaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        model.addAttribute("consulta", consulta);

        model.addAttribute("diagnosticosList", diagnosticoService.listarTodos());

        return "consulta/editar-consulta";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String atualizarConsulta(
            @PathVariable Long id,
            @ModelAttribute("consulta") Consulta dados,
            @RequestParam(value = "diagnosticos", required = false) List<Long> diagnosticosIds,
            RedirectAttributes redirect) {

        try {
            Consulta consulta = consultaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

            consulta.setObservacoes(dados.getObservacoes());
            consulta.setStatus(dados.getStatus());

            if (diagnosticosIds != null && !diagnosticosIds.isEmpty()) {
                consulta.setDiagnosticos(
                        new HashSet<>(diagnosticoService.findByIds(diagnosticosIds)));
            } else {
                consulta.getDiagnosticos().clear();
            }

            consultaService.save(consulta);

            redirect.addFlashAttribute("mensagemSucesso", "Consulta atualizada com sucesso!");
            return "redirect:/consultas";

        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao atualizar consulta: " + e.getMessage());
            return "redirect:/consultas/edit/" + id;
        }
    }

    @GetMapping("/cancelar/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String cancelar(
            @PathVariable Long id,
            Authentication auth,
            RedirectAttributes attributes) {

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        try {

            Consulta consulta = consultaService.findById(id)
                    .orElseThrow(() -> new AgendamentoException("Consulta não encontrada."));

            if (principal.hasRole("ROLE_PACIENTE")) {

                Paciente paciente = principal.getPaciente()
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

                if (!consulta.getPaciente().getId().equals(paciente.getId())) {
                    attributes.addFlashAttribute("mensagemErro",
                            "Você não pode cancelar consultas de outro paciente.");
                    return "redirect:/dashboard";
                }
            }

            consultaService.cancelar(id);

            attributes.addFlashAttribute("mensagemSucesso", "Consulta cancelada com sucesso!");

        } catch (AgendamentoException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/detalhes/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public String detalhesConsulta(@PathVariable Long id, Model model) {

        Consulta consulta = consultaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        model.addAttribute("consulta", consulta);

        return "consulta/detalhes-consulta";
    }

}
