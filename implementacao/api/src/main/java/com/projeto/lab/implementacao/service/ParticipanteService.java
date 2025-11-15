package com.projeto.lab.implementacao.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.lab.implementacao.dto.BlocoTransacao;
import com.projeto.lab.implementacao.dto.TransacaoAgrupadaResponse;
import com.projeto.lab.implementacao.exception.ParticipanteException;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.model.Transacao;
import com.projeto.lab.implementacao.repository.ParticipanteRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipanteService {
    private final ParticipanteRepository participanteRepository;

    public double consultarSaldo(Long participanteId) {
        Participante participante = buscarPorId(participanteId);
        return participante.getSaldoMoedas();
    }

    public List<Participante> listarTodos() {
        return participanteRepository.findAll();
    }

    public boolean existeParticipanteComCpf(String cpf) {
        return participanteRepository.findByCpf(cpf).isPresent();
    }

    public boolean temSaldoSuficiente(Long participanteId, Double valor) {
        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(
                        () -> new ParticipanteException("Participante com ID " + participanteId + " não encontrado"));

        if (valor < 0) {
            double valorAbsoluto = Math.abs(valor);
            return participante.getSaldoMoedas() >= valorAbsoluto;
        }

        return true;
    }

    @Transactional
    public void atualizarSaldo(Long participanteId, Double valor) {
        if (valor == null || valor == 0) {
            throw new ParticipanteException("O valor para atualizar o saldo deve ser diferente de zero.");
        }

        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new ParticipanteException("Participante não encontrado"));
        if (valor < 0 && !temSaldoSuficiente(participanteId, valor)) {
            throw new ParticipanteException("Saldo insuficiente para realizar o saque.");
        }

        double novoSaldo = participante.getSaldoMoedas() + valor;
        if (novoSaldo < 0) {
            throw new ParticipanteException("Operação inválida: saldo não pode ser negativo.");
        }

        participante.setSaldoMoedas(novoSaldo);
        participanteRepository.save(participante);
    }

    public Participante buscarPorId(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new ParticipanteException("Participante com ID " + id + " não encontrado"));
    }

    public TransacaoAgrupadaResponse consultarExtrato(Long participanteId) {
        Participante participante = buscarPorId(participanteId);

        List<Transacao> transacoes = new ArrayList<>();
        if (participante.getTransacoesComoPagador() != null) {
            transacoes.addAll(participante.getTransacoesComoPagador());
        }
        if (participante.getTransacoesComoRecebedor() != null) {
            transacoes.addAll(participante.getTransacoesComoRecebedor());
        }

        Map<LocalDate, List<BlocoTransacao>> transacoesAgrupadas = transacoes.stream()
                .filter(t -> t.getData() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getData().toLocalDate(),
                        Collectors.mapping(
                                t -> criarBlocoTransacao(t, participante),
                                Collectors.toList())));

        return new TransacaoAgrupadaResponse(List.of(transacoesAgrupadas));
    }

    private BlocoTransacao criarBlocoTransacao(Transacao transacao, Participante participante) {
        boolean isPagador = transacao.getPagador() != null
                && transacao.getPagador().getId().equals(participante.getId());

        String tipo;
        Double valor;
        String origem;

        if (isPagador) {
            tipo = "SAIDA";
            valor = -Math.abs(transacao.getValor());
            origem = transacao.getRecebedor() != null ? transacao.getRecebedor().getNome() : "Desconhecido";
        } else {
            tipo = "ENTRADA";
            valor = Math.abs(transacao.getValor());
            origem = transacao.getPagador() != null ? transacao.getPagador().getNome() : "Desconhecido";
        }

        return new BlocoTransacao(
                transacao.getData().toLocalTime(),
                valor,
                tipo,
                origem);
    }

}
