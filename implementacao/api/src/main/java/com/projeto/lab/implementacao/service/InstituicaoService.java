package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Instituicao;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.repository.InstituicaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstituicaoService {
    private final InstituicaoRepository instituicaoRepository;

    public Instituicao criar(Instituicao instituicao) {
        if (instituicao.getParticipantes() == null) {
            instituicao.setParticipantes(new ArrayList<>());
        }
        return instituicaoRepository.save(instituicao);
    }

    public Optional<Instituicao> buscarPorId(Long id) {
        return instituicaoRepository.findById(id);
    }

    public Optional<Instituicao> buscarPorCnpj(String cnpj) {
        return instituicaoRepository.findByCnpj(cnpj);
    }

    public Optional<Instituicao> buscarPorNome(String nome) {
        return instituicaoRepository.findByNome(nome);
    }

    public List<Instituicao> listarTodas() {
        return instituicaoRepository.findAll();
    }

    public Instituicao atualizar(Instituicao instituicao) {
        return instituicaoRepository.save(instituicao);
    }

    public void deletar(Long id) {
        instituicaoRepository.deleteById(id);
    }

    @Transactional
    public Instituicao adicionarParticipante(Long instituicaoId, Participante participante) {
        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
        instituicao.getParticipantes().add(participante);
        return instituicaoRepository.save(instituicao);
    }

    public List<Participante> listarParticipantes(Long instituicaoId) {
        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
        return instituicao.getParticipantes();
    }
}
