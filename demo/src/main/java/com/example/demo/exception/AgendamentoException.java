package com.example.demo.exception;

/**
 * Exceção customizada para erros de Regras de Negócio no processo de
 * agendamento e cancelamento.
 */
public class AgendamentoException extends RuntimeException {

    // Construtor que recebe a mensagem de erro
    public AgendamentoException(String message) {
        super(message);
    }

    // Opcional: Construtor que recebe a mensagem e a causa (stack trace)
    public AgendamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}