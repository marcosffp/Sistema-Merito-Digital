package com.projeto.lab.implementacao.dto;

import java.util.List;

public record EmpresaResumoResponse(
    String cnpj,
    String nome,
    List<VantagemResponse> vantagens
) {}