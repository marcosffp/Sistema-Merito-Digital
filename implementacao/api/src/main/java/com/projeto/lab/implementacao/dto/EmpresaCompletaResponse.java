package com.projeto.lab.implementacao.dto;

import java.util.List;

public record EmpresaCompletaResponse(
    Long id,
    String nome,
    String email,
    String cnpj,
    String endereco,
    List<VantagemResponse> vantagens
) {}