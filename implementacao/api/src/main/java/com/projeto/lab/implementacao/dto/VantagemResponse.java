package com.projeto.lab.implementacao.dto;

public record VantagemResponse(
    Long id,
    String nome,
    String descricao,
    Double custo,
    String imagem,
    Long empresaId,
    String empresaNome,
    Integer estoque,
    Boolean disponivel
) {}