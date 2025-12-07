package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatBotController {

    @GetMapping("/chatbot")
    public String abrirChatbot() {
        return "chatbot";
    }
}
