package com.projeto.lab.implementacao.dto;

import java.util.List;

public record ProfessorCompletoResponse(
    Long id,
    String nome,
    String email,
    String cpf,
    String departamento,
    Double saldoMoedas,
    String nomeInstituicao,
    List<DistribuicaoResponse> distribuicoes
) {}