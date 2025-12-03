package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    
    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private HorarioDisponivel horario;
    
    // Relacionamento com User (paciente)
    //aqui esat dadno erro pode ser porqeu eu ainda não criei o model de pacientte
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private User paciente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConsulta status = StatusConsulta.AGENDADA;
    
    @Column(length = 500)
    private String observacoes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    private LocalDateTime dataAtualizacao;
    
    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    // Getters e Setters
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
    
    public HorarioDisponivel getHorario() {
        return horario;
    }
    
    public void setHorario(HorarioDisponivel horario) {
        this.horario = horario;
    }
    
    public User getPaciente() {
        return paciente;
    }
    
    public void setPaciente(User paciente) {
        this.paciente = paciente;
    }
    
    // Métodos de conveniência para acessar dados do paciente
    public String getPacienteNome() {
        return paciente != null ? paciente.getName() : null;
    }
    
    public String getPacienteEmail() {
        return paciente != null ? paciente.getEmail() : null;
    }
    
    public String getPacienteTelefone() {
        return paciente != null ? paciente.getTelefone() : null;
    }
    
    public String getPacienteCpf() {
        return paciente != null ? paciente.getCpf() : null;
    }
    
    public StatusConsulta getStatus() {
        return status;
    }
    
    public void setStatus(StatusConsulta status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public boolean isPendente() {
        return this.status == StatusConsulta.AGENDADA;
    }
    
    public boolean isConcluida() {
        return this.status == StatusConsulta.CONCLUIDA;
    }
    
    public boolean isCancelada() {
        return this.status == StatusConsulta.CANCELADA;
    }
    
    public void cancelar() {
        this.status = StatusConsulta.CANCELADA;
    }
    
    public void concluir() {
        this.status = StatusConsulta.CONCLUIDA;
    }
}