package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.exception.InstituicaoException;
import com.projeto.lab.implementacao.model.Instituicao;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.repository.InstituicaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public Instituicao buscarPorId(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoException("Instituição com ID " + id + " não encontrada"));
    }

    public Instituicao buscarPorCnpj(String cnpj) {
        return instituicaoRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new InstituicaoException("Instituição com CNPJ " + cnpj + " não encontrada"));
    }

    public Instituicao buscarPorNome(String nome) {
        return instituicaoRepository.findByNome(nome)
                .orElseThrow(() -> new InstituicaoException("Instituição com nome '" + nome + "' não encontrada"));
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
        Instituicao instituicao = buscarPorId(instituicaoId);
        instituicao.getParticipantes().add(participante);
        return instituicaoRepository.save(instituicao);
    }

    public List<Participante> listarParticipantes(Long instituicaoId) {
        Instituicao instituicao = buscarPorId(instituicaoId);
        return instituicao.getParticipantes();
    }
}
