package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SecurityControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new SecurityController())
            .build();

    @Test
    void deveRetornarPaginaAcessoNegadoComMensagem() throws Exception {
        mockMvc.perform(get("/acesso-negado"))
                .andExpect(status().isOk())
                .andExpect(view().name("acesso-negado"))
                .andExpect(model().attributeExists("titulo"))
                .andExpect(model().attributeExists("mensagem"));
    }
}
