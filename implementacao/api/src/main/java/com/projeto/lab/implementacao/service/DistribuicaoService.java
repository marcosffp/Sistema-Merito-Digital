package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.exception.DistribuicaoException;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.repository.DistribuicaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistribuicaoService {
    private final DistribuicaoRepository distribuicaoRepository;
    private final ParticipanteService participanteService;

    @Transactional
    public Distribuicao cadastrarDistribuicao(Long professorId, Long alunoId, Double valor, String motivo) {
        Participante participanteProfessor = participanteService.buscarPorId(professorId);
        Participante participanteAluno = participanteService.buscarPorId(alunoId);

        if (participanteProfessor.getSaldoMoedas() < valor) {
            throw new DistribuicaoException("Saldo insuficiente para distribuir moedas");
        }

        participanteService.atualizarSaldo(alunoId, valor);
        participanteService.atualizarSaldo(professorId, -Math.abs(valor));

        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setCodigo(UUID.randomUUID().toString());
        distribuicao.setData(LocalDateTime.now());
        distribuicao.setValor(valor);
        distribuicao.setMotivo(motivo);
        distribuicao.setRecebedor(participanteAluno);
        distribuicao.setPagador(participanteProfessor);

        Distribuicao salva = distribuicaoRepository.save(distribuicao);

        salva.enviarNotificacao();

        return salva;
    }

    public Optional<Distribuicao> buscarPorId(Long id) {
        return distribuicaoRepository.findById(id);
    }

    public Optional<Distribuicao> buscarPorCodigo(String codigo) {
        return distribuicaoRepository.findByCodigo(codigo);
    }

    public List<Distribuicao> listarTodas() {
        return distribuicaoRepository.findAll();
    }

    public void deletar(Long id) {
        distribuicaoRepository.deleteById(id);
    }
}
