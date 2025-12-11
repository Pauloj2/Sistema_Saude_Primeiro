package com.example.demo.controller;

import com.example.demo.model.Paciente;
import com.example.demo.model.User;
import com.example.demo.service.PacienteService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@Import(TestSecurityConfig.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private UserService userService;

    @Test
    void deveListarPacientes() throws Exception {
        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/index"))
                .andExpect(model().attributeExists("pacientesList"));
    }

    @Test
    void deveAbrirFormularioCadastro() throws Exception {
        mockMvc.perform(get("/pacientes/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/create"))
                .andExpect(model().attributeExists("paciente"));
    }

    @Test
    void deveSalvarPaciente() throws Exception {
        mockMvc.perform(post("/pacientes/save")
                        .param("nome", "João")
                        .param("cpf", "123")
                        .param("telefone", "9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    @Test
    void deveEditarPaciente() throws Exception {
        Mockito.when(pacienteService.findById(1L))
                .thenReturn(new Paciente());

        mockMvc.perform(get("/pacientes/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/edit"))
                .andExpect(model().attributeExists("paciente"));
    }

    @Test
    void deveAtualizarPaciente() throws Exception {
        Paciente paciente = new Paciente();
        paciente.setUser(new User());

        Mockito.when(pacienteService.findById(1L))
                .thenReturn(paciente);

        mockMvc.perform(post("/pacientes/update/1")
                        .param("nome", "Novo Nome")
                        .param("cpf", "111")
                        .param("telefone", "8888"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }

    @Test
    void deveExcluirPaciente() throws Exception {
        mockMvc.perform(get("/pacientes/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));
    }
}
