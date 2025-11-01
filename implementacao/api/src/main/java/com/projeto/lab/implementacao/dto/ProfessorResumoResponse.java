package com.projeto.lab.implementacao.dto;

public record ProfessorResumoResponse(
    String nome,
    String nomeInstituicao,
    Double saldoMoedas,
    String departamento
) {}