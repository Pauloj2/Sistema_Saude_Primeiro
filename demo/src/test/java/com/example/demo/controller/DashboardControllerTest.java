package com.example.demo.controller;

import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@Import(TestSecurityConfig.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;
    @MockBean
    private HorarioDisponivelService horarioService;
    @MockBean
    private ConsultaService consultaService;
    @MockBean
    private MedicamentoService medicamentoService;

    @Test
    void deveAbrirDashboardAtendente() throws Exception {

        Mockito.when(medicoService.count()).thenReturn(2L);
        Mockito.when(horarioService.count()).thenReturn(5L);
        Mockito.when(horarioService.countDisponiveis()).thenReturn(3L);
        Mockito.when(consultaService.count()).thenReturn(1L);
        Mockito.when(medicamentoService.count()).thenReturn(10L);
        Mockito.when(medicoService.getAllMedicos()).thenReturn(List.of());

        CustomUserDetails user = Mockito.mock(CustomUserDetails.class);
        Mockito.when(user.hasRole("PACIENTE")).thenReturn(false);
        Mockito.when(user.hasRole("ATENDENTE")).thenReturn(true);

        mockMvc.perform(get("/dashboard")
                .principal(new UsernamePasswordAuthenticationToken(user, null, List.of())))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("totalMedicos"))
                .andExpect(model().attributeExists("totalHorarios"))
                .andExpect(model().attributeExists("horariosDisponiveis"))
                .andExpect(model().attributeExists("horariosAgendados"))
                .andExpect(model().attributeExists("totalMedicamentos"))
                .andExpect(model().attributeExists("medicos"));
    }

}
