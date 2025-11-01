package com.projeto.lab.implementacao.dto;

import java.time.LocalDateTime;

public record DistribuicaoResponse(
    Long id,
    String motivo,
    LocalDateTime data,
    Double valor,
    String nomeAluno
) {}