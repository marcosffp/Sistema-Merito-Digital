package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Resgate;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.ResgateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResgateService {
    private final ResgateRepository resgateRepository;
    private final AlunoService alunoService;
    private final VantagemService vantagemService;

    @Transactional
    public Resgate resgatar(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);

        Vantagem vantagem = vantagemService.buscarPorId(vantagemId)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada"));

        if (aluno.getSaldoMoedas() < vantagem.getCusto()) {
            throw new RuntimeException("Saldo insuficiente");
        }

        alunoService.descontarMoedas(alunoId, vantagem.getCusto());

        Resgate resgate = new Resgate();
        resgate.setCodigo(UUID.randomUUID().toString());
        resgate.setCupom(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        resgate.setData(LocalDateTime.now());
        resgate.setValor(vantagem.getCusto());
        resgate.setStatus("CONCLUIDO");

        Resgate salvo = resgateRepository.save(resgate);

        salvo.enviarNotificacao();

        return salvo;
    }

    public Optional<Resgate> buscarPorId(Long id) {
        return resgateRepository.findById(id);
    }

    public Optional<Resgate> buscarPorCupom(String cupom) {
        return resgateRepository.findByCupom(cupom);
    }

    public Optional<Resgate> buscarPorCodigo(String codigo) {
        return resgateRepository.findByCodigo(codigo);
    }

    public List<Resgate> listarTodos() {
        return resgateRepository.findAll();
    }

    public void deletar(Long id) {
        resgateRepository.deleteById(id);
    }

    @Transactional
    public Resgate validarCupom(String cupom) {
        Resgate resgate = resgateRepository.findByCupom(cupom)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        if (!"CONCLUIDO".equals(resgate.getStatus())) {
            throw new RuntimeException("Cupom já utilizado ou inválido");
        }

        resgate.setStatus("UTILIZADO");
        return resgateRepository.save(resgate);
    }
}
