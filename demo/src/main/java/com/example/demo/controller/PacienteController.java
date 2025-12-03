package com.example.demo.controller;

import com.example.demo.model.Paciente;
import com.example.demo.service.PacienteService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientesList", pacienteService.findAll());
        return "paciente/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/create";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("paciente") Paciente paciente,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            return "paciente/create";
        }

        pacienteService.save(paciente);
        return "redirect:/pacientes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.findById(id);
        model.addAttribute("paciente", paciente);
        return "paciente/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("paciente") Paciente pacienteAtualizado,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            return "paciente/edit";
        }

        Paciente paciente = pacienteService.findById(id);

        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setEmail(pacienteAtualizado.getEmail());
        paciente.setCpf(pacienteAtualizado.getCpf());
        paciente.setTelefone(pacienteAtualizado.getTelefone());

        pacienteService.save(paciente);

        return "redirect:/pacientes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return "redirect:/pacientes";
    }
}
