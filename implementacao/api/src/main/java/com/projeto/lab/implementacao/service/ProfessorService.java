package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.exception.ProfessorException;
import com.projeto.lab.implementacao.model.Professor;
import com.projeto.lab.implementacao.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import com.projeto.lab.implementacao.dto.ProfessorResumoResponse;
import com.projeto.lab.implementacao.dto.ProfessorCompletoResponse;
import com.projeto.lab.implementacao.dto.ProfessorUpdateRequest;
import com.projeto.lab.implementacao.mapper.ProfessorMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private static final Double MOEDAS_POR_SEMESTRE = 1000.0;
    private final PasswordEncoder passwordEncoder;
    private final ProfessorMapper professorMapper;

    public Professor criar(Professor professor) {
        professor.setSaldoMoedas(MOEDAS_POR_SEMESTRE);
        return professorRepository.save(professor);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorException("Professor com ID " + id + " não encontrado"));
    }

    public Professor buscarPorEmail(String email) {
        return professorRepository.findByEmail(email)
                .orElseThrow(() -> new ProfessorException("Professor com email " + email + " não encontrado"));
    }

    public Professor buscarPorCpf(String cpf) {
        return professorRepository.findByCpf(cpf)
                .orElseThrow(() -> new ProfessorException("Professor com CPF " + cpf + " não encontrado"));
    }

    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    public Professor atualizar(Professor professor) {
        return professorRepository.save(professor);
    }

    public List<ProfessorResumoResponse> listarTodosResumido() {
        List<Professor> professores = professorRepository.findAll();
        return professores.stream()
                .map(professorMapper::toResumoResponse)
                .toList();
    }

    public ProfessorCompletoResponse obterDadosCompletosProfessor(Long id) {
        Professor professor = buscarPorId(id);
        return professorMapper.toCompletoResponse(professor);
    }

    public void deletar(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ProfessorException("Professor com ID " + id + " não encontrado para exclusão");
        }
        professorRepository.deleteById(id);
    }

    @Transactional
    public ProfessorCompletoResponse updateProfessor(Long id, ProfessorUpdateRequest dto) {
        Professor professor = buscarPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            professor.setNome(dto.nome());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            professorRepository.findByEmail(dto.email()).ifPresent(existingProfessor -> {
                if (!existingProfessor.getId().equals(id)) {
                    throw new ProfessorException("O email " + dto.email() + " já está em uso por outro professor.");
                }
            });
            professor.setEmail(dto.email());
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            professor.setSenha(passwordEncoder.encode(dto.senha()));
        }
        if (dto.cpf() != null && !dto.cpf().isBlank()) {
            professorRepository.findByCpf(dto.cpf()).ifPresent(existingProfessor -> {
                if (!existingProfessor.getId().equals(id)) {
                    throw new ProfessorException("O CPF " + dto.cpf() + " já está em uso por outro professor.");
                }
            });
            professor.setCpf(dto.cpf());
        }
        if (dto.departamento() != null && !dto.departamento().isBlank()) {
            professor.setDepartamento(dto.departamento());
        }

        Professor professorAtualizado = professorRepository.save(professor);
        return professorMapper.toCompletoResponse(professorAtualizado);
    }

    @Transactional
    public void distribuirMoedas(Long professorId, Double valor) {
        Professor professor = buscarPorId(professorId);
        if (professor.getSaldoMoedas() < valor) {
            throw new ProfessorException("Saldo insuficiente para distribuir moedas");
        }
        professor.setSaldoMoedas(professor.getSaldoMoedas() - valor);
        professorRepository.save(professor);
    }

    @Transactional
    public void adicionarMoedasSemestre(Long professorId) {
        Professor professor = buscarPorId(professorId);
        professor.setSaldoMoedas(professor.getSaldoMoedas() + MOEDAS_POR_SEMESTRE);
        professorRepository.save(professor);
    }

    public Double consultarSaldo(Long professorId) {
        Professor professor = buscarPorId(professorId);
        return professor.getSaldoMoedas();
    }
}
