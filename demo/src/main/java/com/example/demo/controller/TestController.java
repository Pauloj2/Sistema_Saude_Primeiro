package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Sistema de Saúde - API está funcionando!";
    }
    
    @GetMapping("/api/test")
    public String test() {
        return "Teste OK - Spring Boot + MySQL conectados!";
    }
}