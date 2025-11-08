package com.projeto.lab.implementacao.exception;

public class VantagemException extends RuntimeException {
    public VantagemException(String message) {
        super(message);
    }

    public VantagemException(String message, Throwable cause) {
        super(message, cause);
    }
}