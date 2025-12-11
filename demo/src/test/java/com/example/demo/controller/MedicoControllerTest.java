package com.example.demo.controller;

import com.example.demo.model.Medico;
import com.example.demo.service.MedicoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
@Import(TestSecurityConfig.class)
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveListarMedicos() throws Exception {
        Mockito.when(medicoService.getAllMedicos()).thenReturn(List.of(new Medico()));

        mockMvc.perform(get("/medico"))
                .andExpect(status().isOk())
                .andExpect(view().name("medico/index"))
                .andExpect(model().attributeExists("medicosList"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveAbrirFormularioCadastro() throws Exception {
        mockMvc.perform(get("/medico/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("medico/create"))
                .andExpect(model().attributeExists("medico"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveSalvarMedicoComSucesso() throws Exception {
        Mockito.when(medicoService.existsByCrm("123")).thenReturn(false);

        mockMvc.perform(post("/medico/save")
                .param("nome", "João")
                .param("crm", "123"))
                .andExpect(status().isOk()) 
                .andExpect(view().name("medico/create"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveEditarMedico() throws Exception {
        Mockito.when(medicoService.getMedicoById(1L)).thenReturn(new Medico());

        mockMvc.perform(get("/medico/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("medico/edit"))
                .andExpect(model().attributeExists("medico"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveExcluirMedico() throws Exception {
        mockMvc.perform(get("/medico/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medico"));
    }
}
