package com.projeto.lab.implementacao.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.projeto.lab.implementacao.dto.AlunoCompletoResponse;
import com.projeto.lab.implementacao.dto.AlunoResumoResponse;
import com.projeto.lab.implementacao.dto.ResgateResponse;
import com.projeto.lab.implementacao.dto.TransacaoRecebimentoDTO;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.model.Resgate;

@Component
public class AlunoMapper {

    public AlunoResumoResponse toResumoResponse(Aluno aluno) {
        return new AlunoResumoResponse(
            aluno.getId(),
            aluno.getNome(),
            aluno.getCurso(),
            aluno.getInstituicao() != null ? aluno.getInstituicao().getNome() : null,
            aluno.getSaldoMoedas()
        );
    }

    public AlunoCompletoResponse toCompletoResponse(Aluno aluno, List<Distribuicao> distribuicoes) {
        String nomeInstituicao = aluno.getInstituicao() != null ? aluno.getInstituicao().getNome() : null;
        
        List<ResgateResponse> resgates = aluno.getTransacoesComoPagador() != null
            ? aluno.getTransacoesComoPagador().stream()
                .filter(transacao -> transacao instanceof Resgate)
                .map(transacao -> (Resgate) transacao)
                .map(this::toResgateResponse)
                .collect(Collectors.toList())
            : List.of();

        List<TransacaoRecebimentoDTO> transacoes = distribuicoes.stream()
            .map(d -> new TransacaoRecebimentoDTO(
                d.getId(),
                d.getValor(),
                d.getMotivo(),
                d.getData(),
                d.getPagador().getNome()
            ))
            .toList();

        return new AlunoCompletoResponse(
            aluno.getId(),
            aluno.getNome(),
            aluno.getEmail(),
            aluno.getCpf(),
            aluno.getRg(),
            aluno.getEndereco(),
            aluno.getCurso(),
            aluno.getSaldoMoedas(),
            nomeInstituicao,
            resgates,
            transacoes
        );
    }

    private ResgateResponse toResgateResponse(Resgate resgate) {
        return new ResgateResponse(
            resgate.getId(),
            resgate.getCupom(),
            resgate.getCodigo(),
            resgate.getData(),
            resgate.getValor(),
            resgate.getVantagem() != null ? resgate.getVantagem().getNome() : null
        );
    }
}