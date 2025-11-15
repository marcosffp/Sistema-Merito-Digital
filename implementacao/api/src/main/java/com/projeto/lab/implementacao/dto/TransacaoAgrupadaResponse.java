package com.projeto.lab.implementacao.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record TransacaoAgrupadaResponse(
    List<Map<LocalDate, List<BlocoTransacao>>> transacoesAgrupadas
) {

}