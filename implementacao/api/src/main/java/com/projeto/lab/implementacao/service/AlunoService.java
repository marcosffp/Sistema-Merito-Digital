package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Resgate;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public Aluno criar(Aluno aluno) {
        aluno.setSaldoMoedas(0.0);
        return alunoRepository.save(aluno);
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    public Optional<Aluno> buscarPorCpf(String cpf) {
        return alunoRepository.findByCpf(cpf);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno atualizar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public void deletar(Long id) {
        alunoRepository.deleteById(id);
    }

    @Transactional
    public void receberMoedas(Long alunoId, Double valor) {
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + valor);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void descontarMoedas(Long alunoId, Double valor) {
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        if (aluno.getSaldoMoedas() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - valor);
        alunoRepository.save(aluno);
    }

    public Double consultarSaldo(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return aluno.getSaldoMoedas();
    }
}
