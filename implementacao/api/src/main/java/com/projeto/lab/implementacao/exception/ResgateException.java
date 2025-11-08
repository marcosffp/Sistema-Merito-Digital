package com.projeto.lab.implementacao.exception;

public class ResgateException extends RuntimeException {
    public ResgateException(String message) {
        super(message);
    }

    public ResgateException(String message, Throwable cause) {
        super(message, cause);
    }
}