package com.projeto.lab.implementacao.dto;

import java.time.LocalTime;

public record BlocoTransacao(
        LocalTime horario,
        Double valor,
        String tipo,
        String origem
    ) {}