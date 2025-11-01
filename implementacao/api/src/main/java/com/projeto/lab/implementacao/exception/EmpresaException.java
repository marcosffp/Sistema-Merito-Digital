package com.projeto.lab.implementacao.exception;

public class EmpresaException extends RuntimeException {
    public EmpresaException(String message) {
        super(message);
    }

    public EmpresaException(String message, Throwable cause) {
        super(message, cause);
    }
}