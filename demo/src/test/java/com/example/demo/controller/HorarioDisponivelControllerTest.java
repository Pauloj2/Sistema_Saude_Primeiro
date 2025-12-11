package com.example.demo.controller;

import com.example.demo.model.HorarioDisponivel;
import com.example.demo.service.HorarioDisponivelService;
import com.example.demo.service.MedicoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HorarioDisponivelController.class)
@Import(TestSecurityConfig.class)
class HorarioDisponivelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HorarioDisponivelService horarioService;

    @MockBean
    private MedicoService medicoService;

    @Test
    void deveListarHorarios() throws Exception {
        Mockito.when(horarioService.findAll()).thenReturn(List.of(new HorarioDisponivel()));

        mockMvc.perform(get("/horarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("horario/index"))
                .andExpect(model().attributeExists("horariosList"));
    }

    @Test
    void deveAbrirFormularioCadastro() throws Exception {
        mockMvc.perform(get("/horarios/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("horario/create"))
                .andExpect(model().attributeExists("horario"))
                .andExpect(model().attributeExists("medicos"));
    }

    @Test
    void deveListarHorariosDisponiveis() throws Exception {
        Mockito.when(horarioService.findTodosDisponiveis()).thenReturn(List.of(new HorarioDisponivel()));

        mockMvc.perform(get("/horarios/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(view().name("horario/disponiveis"))
                .andExpect(model().attributeExists("horariosList"));
    }

    @Test
    void deveListarPorMedico() throws Exception {
        Mockito.when(horarioService.findByMedicoId(1L)).thenReturn(List.of(new HorarioDisponivel()));
        Mockito.when(medicoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/horarios/medico/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("horario/por-medico"))
                .andExpect(model().attributeExists("horariosList"))
                .andExpect(model().attributeExists("medico"));
    }
}
