package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.NotNull;

public record ResgateRequest(
    @NotNull(message = "O ID do aluno é obrigatório") Long alunoId,
    @NotNull(message = "O ID da vantagem é obrigatório") Long vantagemId
) {
}