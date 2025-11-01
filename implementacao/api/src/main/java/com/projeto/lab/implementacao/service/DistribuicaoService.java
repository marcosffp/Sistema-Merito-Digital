package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.model.Professor;
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
    private final ProfessorService professorService;
    private final AlunoService alunoService;

    @Transactional
    public Distribuicao distribuirMoedas(Long professorId, Long alunoId, Double valor, String motivo) {
        Professor professor = professorService.buscarPorId(professorId);

        if (professor.getSaldoMoedas() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Aluno aluno = alunoService.buscarPorId(alunoId);

        professorService.distribuirMoedas(professorId, valor);

        alunoService.receberMoedas(alunoId, valor);

        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setCodigo(UUID.randomUUID().toString());
        distribuicao.setData(LocalDateTime.now());
        distribuicao.setValor(valor);
        distribuicao.setStatus("CONCLUIDA");
        distribuicao.setMotivo(motivo);

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
