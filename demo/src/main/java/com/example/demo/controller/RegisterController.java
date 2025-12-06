package com.example.demo.controller;

import com.example.demo.dto.RegistroDTO;
import com.example.demo.model.Paciente;
import com.example.demo.model.User;
import com.example.demo.service.PacienteService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final PacienteService pacienteService;
    private final UserService userService;

    public RegisterController(PacienteService pacienteService,
            UserService userService) {
        this.pacienteService = pacienteService;
        this.userService = userService;
    }

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO());
        return "auth/register";
    }

    @PostMapping
    public String registrarPaciente(
            @ModelAttribute("registroDTO") RegistroDTO registroDTO,
            RedirectAttributes redirectAttributes) {

        if (userService.emailExiste(registroDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("erro", "Este e-mail já está cadastrado.");
            return "redirect:/register";
        }

        if (pacienteService.cpfExiste(registroDTO.getCpf())) {
            redirectAttributes.addFlashAttribute("erro", "Este CPF já está cadastrado.");
            return "redirect:/register";
        }

        User user = new User();
        user.setName(registroDTO.getNome());
        user.setEmail(registroDTO.getEmail());
        user.setPassword(registroDTO.getSenha());
        user.setRole("ROLE_PACIENTE");

        User savedUser = userService.save(user);

        Paciente paciente = new Paciente();
        paciente.setNome(registroDTO.getNome());
        paciente.setCpf(registroDTO.getCpf());
        paciente.setTelefone(registroDTO.getTelefone());
        paciente.setUser(savedUser);

        pacienteService.save(paciente);

        redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça login.");
        return "redirect:/login";
    }
}
