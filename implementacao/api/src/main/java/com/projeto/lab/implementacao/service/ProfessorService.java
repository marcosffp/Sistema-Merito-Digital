package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.exception.ProfessorException;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.model.Professor;
import com.projeto.lab.implementacao.repository.DistribuicaoRepository;
import com.projeto.lab.implementacao.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import com.projeto.lab.implementacao.dto.ProfessorResumoResponse;
import com.projeto.lab.implementacao.dto.ProfessorCompletoResponse;
import com.projeto.lab.implementacao.dto.ProfessorUpdateRequest;
import com.projeto.lab.implementacao.mapper.ProfessorMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private static final Double MOEDAS_POR_SEMESTRE = 1000.0;
    private final PasswordEncoder passwordEncoder;
    private final ProfessorMapper professorMapper;
    private final ParticipanteService participanteService;
    private final DistribuicaoRepository distribuicaoRepository;

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
            if (participanteService.existeParticipanteComCpf(dto.cpf())) {
                throw new ProfessorException("O CPF " + dto.cpf() + " já está em uso por outro participante.");
            }
            professor.setCpf(dto.cpf());
        }
        if (dto.departamento() != null && !dto.departamento().isBlank()) {
            professor.setDepartamento(dto.departamento());
        }

        Professor professorAtualizado = professorRepository.save(professor);
        return professorMapper.toCompletoResponse(professorAtualizado);
    }

    @Transactional
    public void adicionarMoedasSemestre(Long professorId) {
        Professor professor = buscarPorId(professorId);
        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setMotivo("Crédito semestral");
        distribuicao.setValor(MOEDAS_POR_SEMESTRE);
        distribuicao.setData(LocalDateTime.now());
        participanteService.atualizarSaldo(professorId, MOEDAS_POR_SEMESTRE);
        distribuicao.setRecebedor(professor);

        professorRepository.save(professor);
    }

    @Transactional
    public void atualizarSaldoSemestral() {
        List<Professor> professores = professorRepository.findAll();

        for (Professor professor : professores) {
            if (professor.getUltimaAtualizacaoSaldo() == null ||
                    professor.getUltimaAtualizacaoSaldo().plusMonths(6).isBefore(LocalDate.now())) {
                processarDistribuicaoSemestral(professor, "Crédito semestral");
            }
        }
    }

    @Transactional
    public void saldosInicializar() {
        List<Professor> professores = professorRepository.findAll();

        for (Professor professor : professores) {
            processarDistribuicaoSemestral(professor, "Crédito inicial");
        }
    }

    private void processarDistribuicaoSemestral(Professor professor, String motivo) {
        Participante participante = participanteService.buscarPorId(professor.getId());
        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setMotivo(motivo);
        distribuicao.setValor(MOEDAS_POR_SEMESTRE);
        distribuicao.setData(LocalDateTime.now());
        distribuicao.setCodigo(UUID.randomUUID().toString());
        distribuicao.setRecebedor(participante);

        participanteService.atualizarSaldo(professor.getId(), MOEDAS_POR_SEMESTRE);
        professor.setUltimaAtualizacaoSaldo(LocalDate.now());
        professorRepository.save(professor);
        distribuicaoRepository.save(distribuicao);
    }
}
