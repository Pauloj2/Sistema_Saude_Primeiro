package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/login",
                                                                "/register",
                                                                "/favicon.ico",
                                                                "/error",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**",
                                                                "/fonts/**",
                                                                "/webjars/**")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/fragments/**")
                                                .permitAll()

                                                .requestMatchers("/api/chatbot").permitAll()

                                                .requestMatchers("/dashboard").hasAnyRole("PACIENTE", "ATENDENTE")

                                                .requestMatchers("/chatbot").hasAnyRole("PACIENTE", "ATENDENTE")

                                                .requestMatchers(
                                                                "/medico/listar",
                                                                "/medico/disponiveis")
                                                .hasAnyRole("PACIENTE", "ATENDENTE")

                                                .requestMatchers(
                                                                "/medico/novo",
                                                                "/medico/editar/**",
                                                                "/medico/excluir/**",
                                                                "/medico/salvar")
                                                .hasRole("ATENDENTE")

                                                .requestMatchers(
                                                                "/consultas/minhas",
                                                                "/consultas/agendar",
                                                                "/consultas/detalhes/**",
                                                                "/consultas/cancelar/**")
                                                .hasRole("PACIENTE")

                                                .requestMatchers(
                                                                "/consultas/listar",
                                                                "/consultas/gerenciar/**",
                                                                "/consultas/atualizar/**")
                                                .hasRole("ATENDENTE")

                                                .requestMatchers("/horarios/**").hasRole("ATENDENTE")

                                                .requestMatchers(
                                                                "/medicamento/consulta", // Pacientes podem consultar
                                                                "/medicamento/listar" // Atendentes podem listar
                                                ).hasAnyRole("PACIENTE", "ATENDENTE")

                                                .requestMatchers(
                                                                "/medicamento/novo",
                                                                "/medicamento/editar/**",
                                                                "/medicamento/excluir/**",
                                                                "/medicamento/salvar")
                                                .hasRole("ATENDENTE")

                                                .requestMatchers("/estoque/**").hasRole("ATENDENTE")

                                                .requestMatchers("/diagnosticos/**").hasRole("ATENDENTE")

                                                .requestMatchers("/pacientes/**").hasRole("ATENDENTE")

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/acesso-negado"));

                return http.build();
        }
}