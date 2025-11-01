package com.projeto.lab.implementacao.exception;

public class InstituicaoException extends RuntimeException {
    public InstituicaoException(String message) {
        super(message);
    }

    public InstituicaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
