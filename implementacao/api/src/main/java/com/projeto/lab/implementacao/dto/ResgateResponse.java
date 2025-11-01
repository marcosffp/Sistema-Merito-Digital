package com.projeto.lab.implementacao.dto;

import java.time.LocalDateTime;

public record ResgateResponse(
    Long id,
    String codigo,
    LocalDateTime data,
    Double valor,
    String status,
    String cupom
) {}