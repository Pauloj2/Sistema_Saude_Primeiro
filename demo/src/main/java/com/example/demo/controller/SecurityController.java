package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/acesso-negado")
    public String acessoNegado(Model model) {
        model.addAttribute("titulo", "Acesso Negado");
        model.addAttribute("mensagem", "Você não tem permissão para acessar esta página.");
        return "acesso-negado";
    }
}