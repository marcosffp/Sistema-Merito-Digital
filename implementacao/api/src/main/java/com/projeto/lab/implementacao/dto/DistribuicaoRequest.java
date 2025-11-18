package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

public record DistribuicaoRequest(
    @NotNull(message = "ID do professor é obrigatório")
    Long professorId,
    
    @NotNull(message = "ID do aluno é obrigatório")
    Long alunoId,
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    Double valor,
    
    @NotBlank(message = "Motivo é obrigatório")
    String motivo
) {}
