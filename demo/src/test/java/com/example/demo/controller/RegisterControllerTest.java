package com.example.demo.controller;

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

@WebMvcTest(RegisterController.class)
@Import(TestSecurityConfig.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private UserService userService;

    @Test
    void deveAbrirFormularioDeRegistro() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("registroDTO"));
    }

    @Test
    void deveImpedirRegistroQuandoEmailJaExiste() throws Exception {
        Mockito.when(userService.emailExiste("teste@email.com")).thenReturn(true);

        mockMvc.perform(post("/register")
                .param("nome", "João")
                .param("email", "teste@email.com")
                .param("senha", "123")
                .param("cpf", "111")
                .param("telefone", "9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("erro"));
    }

    @Test
    void deveImpedirRegistroQuandoCpfJaExiste() throws Exception {
        Mockito.when(userService.emailExiste("novo@email.com")).thenReturn(false);
        Mockito.when(pacienteService.cpfExiste("111")).thenReturn(true);

        mockMvc.perform(post("/register")
                .param("nome", "João")
                .param("email", "novo@email.com")
                .param("senha", "123")
                .param("cpf", "111")
                .param("telefone", "9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("erro"));
    }

    @Test
    void deveRegistrarPacienteComSucesso() throws Exception {

        Mockito.when(userService.emailExiste("novo@email.com")).thenReturn(false);
        Mockito.when(pacienteService.cpfExiste("222")).thenReturn(false);

        User user = new User();
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                .param("nome", "João")
                .param("email", "novo@email.com")
                .param("senha", "123")
                .param("cpf", "222")
                .param("telefone", "9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("sucesso"));
    }
}
