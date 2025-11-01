package com.projeto.lab.implementacao.exception;

public class ProfessorException extends RuntimeException {
    public ProfessorException(String message) {
        super(message);
    }

    public ProfessorException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
