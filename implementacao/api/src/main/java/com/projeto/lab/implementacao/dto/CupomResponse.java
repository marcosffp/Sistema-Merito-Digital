package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.NotBlank;

public record CupomResponse(
        @NotBlank(message = "O cupom do aluno é obrigatório.") String cupomAluno,
        @NotBlank(message = "O cupom do parceiro é obrigatório.") String cupomParceiro) {

}
