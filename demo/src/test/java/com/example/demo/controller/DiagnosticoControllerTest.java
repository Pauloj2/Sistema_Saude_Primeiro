package com.example.demo.controller;

import com.example.demo.model.Diagnostico;
import com.example.demo.service.DiagnosticoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiagnosticoController.class)
@Import(TestSecurityConfig.class)
class DiagnosticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiagnosticoService diagnosticoService;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveListarDiagnosticos() throws Exception {
        Mockito.when(diagnosticoService.listarTodos())
                .thenReturn(List.of(new Diagnostico()));

        mockMvc.perform(get("/diagnosticos"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnostico/listar"))
                .andExpect(model().attributeExists("lista"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAbrirFormularioNovo() throws Exception {
        mockMvc.perform(get("/diagnosticos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnostico/form"))
                .andExpect(model().attributeExists("diagnostico"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveSalvarDiagnostico() throws Exception {
        mockMvc.perform(post("/diagnosticos/salvar")
                        .param("descricao", "Teste"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diagnosticos"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveExcluirDiagnostico() throws Exception {
        mockMvc.perform(get("/diagnosticos/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diagnosticos"));
    }
}
