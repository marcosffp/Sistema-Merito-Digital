package com.projeto.lab.implementacao.dto;

public record ResgateResponse(
    Long id,
    String cupom,
    String codigo,
    Double valor,
    String alunoNome,
    String vantagemNome
) {}