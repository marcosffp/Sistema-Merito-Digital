package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public record VantagemRequest(
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    String descricao,

    @NotNull(message = "O custo é obrigatório.")
    @Positive(message = "O custo deve ser um valor positivo.")
    Double custo,

    @NotNull(message = "O ID da empresa é obrigatório.")
    Long empresaId,

    @NotNull(message = "O estoque é obrigatório.")
    Integer estoque
) {}