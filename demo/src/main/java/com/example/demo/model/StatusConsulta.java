package com.example.demo.model;

public enum StatusConsulta {
    AGENDADA("Agendada"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
    
    private final String descricao;
    
    StatusConsulta(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public boolean isFinalizada() {
        return this == CONCLUIDA || this == CANCELADA;
    }
}