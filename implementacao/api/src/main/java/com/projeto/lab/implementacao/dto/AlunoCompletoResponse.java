package com.projeto.lab.implementacao.dto;

import java.util.List;

public record AlunoCompletoResponse(
    Long id,
    String nome,
    String email,
    String cpf,
    String rg,
    String endereco,
    String curso,
    Double saldoMoedas,
    String nomeInstituicao,
    List<ResgateResponse> resgates
) {}