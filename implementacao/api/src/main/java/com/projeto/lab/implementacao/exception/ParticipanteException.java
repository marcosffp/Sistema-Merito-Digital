package com.projeto.lab.implementacao.exception;

public class ParticipanteException extends RuntimeException {
    public ParticipanteException(String message) {
        super(message);
    }
    
    public ParticipanteException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
