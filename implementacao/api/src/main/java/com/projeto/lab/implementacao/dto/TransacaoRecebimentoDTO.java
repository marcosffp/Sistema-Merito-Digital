package com.projeto.lab.implementacao.dto;

import java.time.LocalDateTime;

public record TransacaoRecebimentoDTO(
    Long id,
    Double valor,
    String motivo,
    LocalDateTime data,
    String nomeProfessor
) {}
