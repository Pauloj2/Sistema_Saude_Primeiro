package com.example.demo.controller;

import com.example.demo.model.Consulta;
import com.example.demo.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    @MockBean
    private MedicoService medicoService;

    @MockBean
    private HorarioDisponivelService horarioService;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private DiagnosticoService diagnosticoService;

    // ============================
    // ✅ LISTAR CONSULTAS (ATENDENTE)
    // ============================
    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveListarTodasConsultas() throws Exception {

        Mockito.when(consultaService.findAll())
                .thenReturn(List.of(new Consulta()));

        mockMvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("consultasList"));
    }

    // ============================================
    // ✅ ABRIR FORMULÁRIO DE AGENDAMENTO (PACIENTE)
    // ============================================
    @Test
    @WithMockUser(roles = "PACIENTE")
    void deveAbrirFormularioAgendamentoComoPaciente() throws Exception {

        Mockito.when(medicoService.findAll()).thenReturn(List.of());
        Mockito.when(horarioService.findTodosDisponiveis()).thenReturn(List.of());

        mockMvc.perform(get("/consultas/agendar"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("consulta"))
                .andExpect(model().attributeExists("medicos"))
                .andExpect(model().attributeExists("horarios"));
    }

    // ============================
    // ✅ EDITAR CONSULTA (ATENDENTE)
    // ============================
    @Test
    @WithMockUser(roles = "ATENDENTE")
    void deveEditarConsulta() throws Exception {

        Mockito.when(consultaService.findById(1L))
                .thenReturn(Optional.of(new Consulta()));

        Mockito.when(diagnosticoService.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/consultas/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("consulta"))
                .andExpect(model().attributeExists("diagnosticosList"));
    }

    // ============================================
    // ✅ BLOQUEIO DE ACESSO SEM ROLE
    // ============================================
    @Test
    @WithMockUser(roles = "PACIENTE")
    void deveBloquearAcessoEditarConsultaParaPaciente() throws Exception {

        mockMvc.perform(get("/consultas/edit/1"))
                .andExpect(status().isForbidden());
    }
}
