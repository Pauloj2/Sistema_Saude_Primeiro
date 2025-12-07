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

                                                // ✅ PUBLICO
                                                .requestMatchers(
                                                                "/login",
                                                                "/register",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**")
                                                .permitAll()

                                                // ✅ DASHBOARD
                                                .requestMatchers("/dashboard").hasAnyRole("PACIENTE", "ATENDENTE")

                                                // ✅ CHATBOT TELA
                                                .requestMatchers("/chatbot").hasAnyRole("PACIENTE", "ATENDENTE")

                                                // ✅ CHATBOT API (MUITO IMPORTANTE)
                                                .requestMatchers("/api/chatbot").permitAll()
                                                
                                                .requestMatchers("/medicamento/consulta")
                                                .hasAnyRole("PACIENTE", "ATENDENTE")

                                                // ✅ ATENDENTE
                                                .requestMatchers(
                                                                "/medico/**",
                                                                "/medicamento/**",
                                                                "/pacientes/**",
                                                                "/horarios/**",
                                                                "/estoque/**",
                                                                "/diagnosticos/**")
                                                .hasRole("ATENDENTE")

                                                // ✅ PACIENTE
                                                .requestMatchers("/consultas/minhas").hasRole("PACIENTE")

                                                // ✅ CONSULTAS GERAIS
                                                .requestMatchers("/consultas/**").authenticated()

                                                .anyRequest().denyAll())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll());

                return http.build();
        }
}
