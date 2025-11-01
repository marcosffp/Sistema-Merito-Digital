package com.projeto.lab.implementacao.exception;

public class AlunoException extends RuntimeException {
    public AlunoException(String message) {
        super(message);
    }

    public AlunoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
