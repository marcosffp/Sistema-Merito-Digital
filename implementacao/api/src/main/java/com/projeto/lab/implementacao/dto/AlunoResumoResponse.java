package com.projeto.lab.implementacao.dto;

public record AlunoResumoResponse(
    Long id,
    String nome,
    String curso,
    String instituicao,
    Double saldoMoedas
) {}