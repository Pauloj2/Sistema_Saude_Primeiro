package com.example.demo.config;

import com.example.demo.model.Paciente;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.PacienteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    private final UserService userService;
    private final PacienteService pacienteService;

    public DataLoader(UserService userService, PacienteService pacienteService) {
        this.userService = userService;
        this.pacienteService = pacienteService;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {

            if (!userService.emailExiste("atendente@saudeprimeiro.com")) {
                System.out.println("Criando Atendente Inicial...");

                User atendenteUser = new User();
                atendenteUser.setName("Atendente Master");
                atendenteUser.setEmail("atendente@saudeprimeiro.com");
                atendenteUser.setPassword("123456");
                atendenteUser.setRole("ROLE_ATENDENTE");
                userService.save(atendenteUser);
            }

            if (!userService.emailExiste("paciente@saudeprimeiro.com")) {
                System.out.println("Criando Paciente Inicial...");

                User pacienteUser = new User();
                pacienteUser.setName("Paciente Teste");
                pacienteUser.setEmail("paciente@saudeprimeiro.com");
                pacienteUser.setPassword("123456");
                pacienteUser.setRole("ROLE_PACIENTE"); 

                User savedUser = userService.save(pacienteUser);

                Paciente paciente = new Paciente();
                paciente.setNome("Paciente Teste");
                paciente.setCpf("11122233344");
                paciente.setTelefone("999999999");
                paciente.setUser(savedUser);

                pacienteService.save(paciente);
            }

            System.out.println("Dados iniciais carregados. ✅");
        };
    }
}
