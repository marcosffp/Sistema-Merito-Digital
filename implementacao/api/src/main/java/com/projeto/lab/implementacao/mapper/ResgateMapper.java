package com.projeto.lab.implementacao.mapper;

import org.springframework.stereotype.Component;

import com.projeto.lab.implementacao.dto.ResgateResponse;
import com.projeto.lab.implementacao.model.Resgate;

@Component
public class ResgateMapper {

    public ResgateResponse toResponse(Resgate resgate) {
        return new ResgateResponse(
            resgate.getId(),
            resgate.getCupom(),
            resgate.getCodigo(),
            resgate.getValor(),
            resgate.getAluno() != null ? resgate.getAluno().getNome() : null, // Nome do aluno
            resgate.getVantagem() != null ? resgate.getVantagem().getNome() : null // Nome da vantagem
        );
    }
}