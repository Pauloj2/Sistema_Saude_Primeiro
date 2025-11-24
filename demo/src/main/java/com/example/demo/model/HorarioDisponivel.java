package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios_disponiveis")
public class HorarioDisponivel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Relacionamento muitos-para-um com a entidade Medico
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    
    // Usa EnumType.STRING para armazenar o nome do enum no banco
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusHorario status = StatusHorario.DISPONIVEL;
    
    public enum StatusHorario {
        DISPONIVEL,    
        AGENDADO,      
        CANCELADO   
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public StatusHorario getStatus() {
        return status;
    }
    
    // Define o status do horário
    public void setStatus(StatusHorario status) {
        this.status = status;
    }
        
    // Verifica se o horário está disponível para agendamento
    public boolean isDisponivel() {
        return this.status == StatusHorario.DISPONIVEL;
    }
    
    // Verifica se o horário já foi agendado
    public boolean isAgendado() {
        return this.status == StatusHorario.AGENDADO;
    }
    
    // Verifica se o horário foi cancelado
    public boolean isCancelado() {
        return this.status == StatusHorario.CANCELADO;
    }
    
    // Método equals para comparação de objetos
    // Dois horários são iguais se possuem o mesmo ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        HorarioDisponivel that = (HorarioDisponivel) o;
        return id != null && id.equals(that.id);
    }
    
}