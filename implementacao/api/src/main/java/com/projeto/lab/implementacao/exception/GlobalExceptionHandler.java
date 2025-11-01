package com.projeto.lab.implementacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle JwtAuthenticationException
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro de Autenticação");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Handle AlunoException
    @ExceptionHandler(AlunoException.class)
    public ResponseEntity<Map<String, String>> handleAlunoException(AlunoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro com Aluno");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle ProfessorException
    @ExceptionHandler(ProfessorException.class)
    public ResponseEntity<Map<String, String>> handleProfessorException(ProfessorException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro com Professor");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle InstituicaoException
    @ExceptionHandler(InstituicaoException.class)
    public ResponseEntity<Map<String, String>> handleInstituicaoException(InstituicaoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro com Instituição");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle validation errors (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro Interno do Servidor");
        response.put("message", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Handle EmpresaException
    @ExceptionHandler(EmpresaException.class)
    public ResponseEntity<Map<String, String>> handleEmpresaException(EmpresaException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Erro com Empresa");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}