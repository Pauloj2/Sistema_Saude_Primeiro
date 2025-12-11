package com.example.demo.controller;

import com.example.demo.service.MedicamentoService;
import com.example.demo.service.MovimentacaoEstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimentacaoEstoqueController.class)
@Import(TestSecurityConfig.class)
class MovimentacaoEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacaoEstoqueService estoqueService;

    @MockBean
    private MedicamentoService medicamentoService;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveListarHistorico() throws Exception {
        mockMvc.perform(get("/estoque"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/historico"))
                .andExpect(model().attributeExists("movimentacoes"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAbrirFormularioEntrada() throws Exception {
        mockMvc.perform(get("/estoque/entrada"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/entrada"))
                .andExpect(model().attributeExists("medicamentos"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveRegistrarEntrada() throws Exception {
        mockMvc.perform(post("/estoque/entrada")
                .param("medicamentoId", "1")
                .param("quantidade", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estoque"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAbrirFormularioSaida() throws Exception {
        mockMvc.perform(get("/estoque/saida"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/saida"))
                .andExpect(model().attributeExists("medicamentos"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveRegistrarSaida() throws Exception {
        mockMvc.perform(post("/estoque/saida")
                .param("medicamentoId", "1")
                .param("quantidade", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estoque"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAbrirFormularioAjuste() throws Exception {
        mockMvc.perform(get("/estoque/ajuste"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/ajuste"))
                .andExpect(model().attributeExists("medicamentos"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveRegistrarAjuste() throws Exception {
        mockMvc.perform(post("/estoque/ajuste")
                .param("medicamentoId", "1")
                .param("quantidade", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estoque"));
    }
}
