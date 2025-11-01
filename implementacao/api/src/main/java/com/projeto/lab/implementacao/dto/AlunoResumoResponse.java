package com.projeto.lab.implementacao.dto;

public record AlunoResumoResponse(
    String nome,
    String curso,
    String nomeInstituicao,
    Double saldoMoedas
) {}