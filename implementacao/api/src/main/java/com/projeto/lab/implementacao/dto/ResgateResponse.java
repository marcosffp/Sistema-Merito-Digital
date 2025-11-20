package com.projeto.lab.implementacao.dto;

public record ResgateResponse(
    Long id,
    String codigo,
    Double valor,
    String alunoNome,
    String vantagemNome
) {}