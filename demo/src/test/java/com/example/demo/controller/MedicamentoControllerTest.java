package com.example.demo.controller;

import com.example.demo.model.Medicamento;
import com.example.demo.service.MedicamentoService;
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

@WebMvcTest(MedicamentoController.class)
@Import(TestSecurityConfig.class)
class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @Test
    @WithMockUser(roles = "PACIENTE")
    void deveConsultarMedicamentos() throws Exception {
        Mockito.when(medicamentoService.buscarPorNome("dip"))
                .thenReturn(List.of(new Medicamento()));

        mockMvc.perform(get("/medicamento/consulta").param("q", "dip"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/consulta"))
                .andExpect(model().attributeExists("resultados"))
                .andExpect(model().attributeExists("query"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveListarMedicamentos() throws Exception {
        Mockito.when(medicamentoService.listarTodos())
                .thenReturn(List.of(new Medicamento()));

        mockMvc.perform(get("/medicamento"))
                .andExpect(status().isOk())
                .andExpect(view().name("medicamento/listar"))
                .andExpect(model().attributeExists("medicamentos"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAbrirFormularioNovo() throws Exception {
        mockMvc.perform(get("/medicamento/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("medicamento/form"))
                .andExpect(model().attributeExists("medicamento"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveSalvarMedicamento() throws Exception {
        mockMvc.perform(post("/medicamento/salvar")
                        .param("nome", "Dipirona"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamento"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveEditarMedicamento() throws Exception {
        Mockito.when(medicamentoService.buscarPorId(1L))
                .thenReturn(new Medicamento());

        mockMvc.perform(get("/medicamento/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("medicamento/form"))
                .andExpect(model().attributeExists("medicamento"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAtualizarMedicamento() throws Exception {
        mockMvc.perform(post("/medicamento/atualizar/1")
                        .param("nome", "Paracetamol"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamento"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveDeletarMedicamento() throws Exception {
        mockMvc.perform(get("/medicamento/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamento"));
    }
}
