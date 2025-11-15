package com.projeto.lab.implementacao.exception;

public class DistribuicaoException extends RuntimeException {
    public DistribuicaoException(String message) {
        super(message);
    }

    public DistribuicaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
