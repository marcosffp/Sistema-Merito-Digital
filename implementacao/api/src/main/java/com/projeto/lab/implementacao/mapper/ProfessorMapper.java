package com.projeto.lab.implementacao.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.projeto.lab.implementacao.dto.ProfessorCompletoResponse;
import com.projeto.lab.implementacao.dto.ProfessorResumoResponse;
import com.projeto.lab.implementacao.dto.DistribuicaoResponse;
import com.projeto.lab.implementacao.model.Professor;
import com.projeto.lab.implementacao.model.Distribuicao;

@Component
public class ProfessorMapper {

    public ProfessorResumoResponse toResumoResponse(Professor professor) {
        return new ProfessorResumoResponse(
            professor.getNome(),
            professor.getInstituicao().getNome(),
            professor.getSaldoMoedas(),
            professor.getDepartamento()
        );
    }

    public ProfessorCompletoResponse toCompletoResponse(Professor professor) {
        String nomeInstituicao = professor.getInstituicao() != null ? professor.getInstituicao().getNome() : null;
        List<DistribuicaoResponse> distribuicoes = professor.getDistribuicoes() != null
            ? professor.getDistribuicoes().stream().map(this::toDistribuicaoResponse).collect(Collectors.toList())
            : List.of();

        return new ProfessorCompletoResponse(
            professor.getId(),
            professor.getNome(),
            professor.getEmail(),
            professor.getCpf(),
            professor.getDepartamento(),
            professor.getSaldoMoedas(),
            nomeInstituicao,
            distribuicoes
        );
    }

    private DistribuicaoResponse toDistribuicaoResponse(Distribuicao distribuicao) {
        return new DistribuicaoResponse(
            distribuicao.getId(),
            distribuicao.getMotivo(),
            distribuicao.getData(),
            distribuicao.getValor(),
            distribuicao.getAluno().getNome()
        );
    }
}