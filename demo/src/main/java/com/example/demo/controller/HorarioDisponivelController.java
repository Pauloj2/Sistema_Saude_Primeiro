package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.model.Medico;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioDisponivelController {

    @Autowired
    private HorarioDisponivelService horarioService;
    
    @Autowired
    private MedicoService medicoService;

    // LISTAR TODOS OS HORÁRIOS
    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("horariosList", horarioService.getAllHorarioDisponivel());
        return "horario/index";
    }

    // FORMULÁRIO DE CADASTRO
    @GetMapping("/create")
    public String mostrarFormularioCadastro(Model model) {
        HorarioDisponivel horario = new HorarioDisponivel();
        
        // Busca todos os médicos para o dropdown
        List<Medico> medicos = medicoService.getAllMedico();
        
        model.addAttribute("horario", horario);
        model.addAttribute("medicos", medicos);
        return "horario/create";
    }

    // SALVAR HORÁRIO (CADASTRO E EDIÇÃO)
    @PostMapping("/save")
    public String salvarHorario(@ModelAttribute("horario") HorarioDisponivel horario) {
        horarioService.saveHorarioDisponivel(horario);
        return "redirect:/horarios";
    }

    // FORMULÁRIO DE EDIÇÃO
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        HorarioDisponivel horario = horarioService.getHorarioDisponivelById(id);
        List<Medico> medicos = medicoService.getAllMedico();
        
        model.addAttribute("horario", horario);
        model.addAttribute("medicos", medicos);
        return "horario/edit";
    }

    // EXCLUIR HORÁRIO
    @GetMapping("/delete/{id}")
    public String excluirHorario(@PathVariable Long id) {
        horarioService.deleteHorarioDisponivelById(id);
        return "redirect:/horarios";
    }

    // HORÁRIOS DISPONÍVEIS (PARA PACIENTES)
    @GetMapping("/disponiveis")
    public String listarHorariosDisponiveis(Model model) {
        // Filtra apenas horários com status DISPONIVEL
        List<HorarioDisponivel> todosHorarios = horarioService.getAllHorarioDisponivel();
        List<HorarioDisponivel> horariosDisponiveis = todosHorarios.stream()
            .filter(horario -> horario.isDisponivel())
            .toList();
            
        model.addAttribute("horariosList", horariosDisponiveis);
        return "horario/disponiveis";
    }
}