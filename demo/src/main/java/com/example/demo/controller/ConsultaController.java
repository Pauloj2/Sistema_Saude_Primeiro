package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Consulta;
import com.example.demo.model.HorarioDisponivel;
import com.example.demo.model.Medico;
import com.example.demo.model.Paciente;
import com.example.demo.model.StatusConsulta;

import com.example.demo.service.ConsultaService;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;
import com.example.demo.service.PacienteService;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private HorarioDisponivelService horarioService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public String listarConsultas(Model model) {
        model.addAttribute("consultasList", consultaService.findAll());
        return "consulta/index";
    }

    @GetMapping("/agendar")
    public String mostrarFormularioAgendamento(Model model) {

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("medicos", medicoService.getAllMedico());

        model.addAttribute("horarios", horarioService.getAllHorarioDisponivel()
                .stream()
                .filter(HorarioDisponivel::isDisponivel)
                .toList());

        model.addAttribute("pacientes", pacienteService.findAll());

        return "consulta/agendar";
    }

    @PostMapping("/save")
    public String salvarConsulta(
            @ModelAttribute("consulta") Consulta consulta,
            @RequestParam("medicoId") Long medicoId,
            @RequestParam("horarioId") Long horarioId,
            @RequestParam("pacienteId") Long pacienteId,
            RedirectAttributes redirectAttributes) {

        try {
            Medico medico = medicoService.getMedicoById(medicoId);
            HorarioDisponivel horario = horarioService.getHorarioDisponivelById(horarioId);
            Paciente paciente = pacienteService.findById(pacienteId);

            consulta.setMedico(medico);
            consulta.setHorario(horario);
            consulta.setPaciente(paciente);

            consultaService.save(consulta);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta agendada com sucesso!");
            return "redirect:/consultas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/consultas/agendar";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String visualizarDetalhes(@PathVariable Long id, Model model) {

        Consulta consulta = consultaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        model.addAttribute("consulta", consulta);
        return "consulta/detalhes";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {

        Consulta consulta = consultaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        model.addAttribute("consulta", consulta);
        return "consulta/edit";
    }

    @PostMapping("/update/{id}")
    public String atualizarConsulta(
            @PathVariable Long id,
            @ModelAttribute("consulta") Consulta consultaAtualizada,
            RedirectAttributes redirectAttributes) {

        Consulta consulta = consultaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        consulta.setObservacoes(consultaAtualizada.getObservacoes());
        consulta.setStatus(consultaAtualizada.getStatus());

        consultaService.save(consulta);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta atualizada com sucesso!");
        return "redirect:/consultas";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarConsulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            Consulta consulta = consultaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

            consulta.cancelar();

            HorarioDisponivel horario = consulta.getHorario();
            horario.setDisponivel(true);
            horarioService.saveHorarioDisponivel(horario);

            consultaService.save(consulta);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta cancelada com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cancelar consulta: " + e.getMessage());
        }

        return "redirect:/consultas";
    }

    @GetMapping("/concluir/{id}")
    public String concluirConsulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            Consulta consulta = consultaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

            consulta.concluir();
            consultaService.save(consulta);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta concluída!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao concluir consulta: " + e.getMessage());
        }

        return "redirect:/consultas";
    }

    @GetMapping("/delete/{id}")
    public String excluirConsulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            consultaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta excluída!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }

        return "redirect:/consultas";
    }

    @GetMapping("/minhas")
    public String minhasConsultas(Model model, Authentication auth) {

        // Recupera o usuário logado (paciente)
        Paciente paciente = (Paciente) auth.getPrincipal();

        model.addAttribute("consultasList",
                consultaService.findByPacienteId(paciente.getId()));

        return "consulta/minhas";
    }

    @GetMapping("/status/{status}")
    public String consultasPorStatus(@PathVariable String status, Model model) {

        StatusConsulta statusEnum = StatusConsulta.valueOf(status.toUpperCase());

        List<Consulta> consultas = consultaService.findAll()
                .stream()
                .filter(c -> c.getStatus() == statusEnum)
                .toList();

        model.addAttribute("consultasList", consultas);
        model.addAttribute("statusFiltro", status);

        return "consulta/index";
    }
}
