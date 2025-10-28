package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Professor;
import com.projeto.lab.implementacao.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private static final Double MOEDAS_POR_SEMESTRE = 1000.0;

    public Professor criar(Professor professor) {
        professor.setSaldoMoedas(MOEDAS_POR_SEMESTRE);
        return professorRepository.save(professor);
    }

    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.findById(id);
    }

    public Optional<Professor> buscarPorEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    public Optional<Professor> buscarPorCpf(String cpf) {
        return professorRepository.findByCpf(cpf);
    }

    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    public Professor atualizar(Professor professor) {
        return professorRepository.save(professor);
    }

    public void deletar(Long id) {
        professorRepository.deleteById(id);
    }

    @Transactional
    public void distribuirMoedas(Long professorId, Double valor) {
        Professor professor = professorRepository.findById(professorId)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        if (professor.getSaldoMoedas() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
        professor.setSaldoMoedas(professor.getSaldoMoedas() - valor);
        professorRepository.save(professor);
    }

    @Transactional
    public void adicionarMoedasSemestre(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        professor.setSaldoMoedas(professor.getSaldoMoedas() + MOEDAS_POR_SEMESTRE);
        professorRepository.save(professor);
    }

    public Double consultarSaldo(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return professor.getSaldoMoedas();
    }
}
