package com.projeto.lab.implementacao.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.lab.implementacao.dto.AlunoRequest;
import com.projeto.lab.implementacao.dto.AlunoResumoResponse;
import com.projeto.lab.implementacao.dto.AlunoUpdateRequest;
import com.projeto.lab.implementacao.dto.AlunoCompletoResponse;
import com.projeto.lab.implementacao.exception.AlunoException;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Instituicao;
import com.projeto.lab.implementacao.repository.AlunoRepository;
import com.projeto.lab.implementacao.mapper.AlunoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstituicaoService instituicaoService;
    private final AlunoMapper alunoMapper;

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoException("Aluno com ID " + id + " não encontrado"));
    }

    public Aluno buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email)
                .orElseThrow(() -> new AlunoException("Aluno com email " + email + " não encontrado"));
    }

    public Aluno buscarPorCpf(String cpf) {
        return alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new AlunoException("Aluno com CPF " + cpf + " não encontrado"));
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public List<AlunoResumoResponse> listarTodosResumido() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(alunoMapper::toResumoResponse)
                .toList();
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
                .orElseThrow(() -> new AlunoException("Aluno não encontrado"));
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + valor);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void descontarMoedas(Long alunoId, Double valor) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new AlunoException("Aluno não encontrado"));
        if (aluno.getSaldoMoedas() < valor) {
            throw new AlunoException("Saldo insuficiente");
        }
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - valor);
        alunoRepository.save(aluno);
    }

    public Double consultarSaldo(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new AlunoException("Aluno não encontrado"));
        return aluno.getSaldoMoedas();
    }

    @Transactional
    public Aluno cadastrarAluno(AlunoRequest dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setSenha(passwordEncoder.encode(dto.senha()));
        aluno.setCpf(dto.cpf());
        aluno.setRg(dto.rg());
        aluno.setEndereco(dto.endereco());
        aluno.setCurso(dto.curso());
        aluno.setSaldoMoedas(0.0);

        Instituicao instituicao = instituicaoService.buscarPorNome(dto.instituicao());
        aluno.setInstituicao(instituicao);

        return alunoRepository.save(aluno);
    }

    public AlunoResumoResponse obterResumoAluno(Long id) {
        Aluno aluno = buscarPorId(id);
        return alunoMapper.toResumoResponse(aluno);
    }

    public AlunoCompletoResponse obterDadosCompletosAluno(Long id) {
        Aluno aluno = buscarPorId(id);
        return alunoMapper.toCompletoResponse(aluno);
    }

    @Transactional
    public AlunoCompletoResponse updateAluno(Long id, AlunoUpdateRequest dto) {
        Aluno aluno = buscarPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            aluno.setNome(dto.nome());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            alunoRepository.findByEmail(dto.email()).ifPresent(existingAluno -> {
                if (!existingAluno.getId().equals(id)) {
                    throw new AlunoException("O email " + dto.email() + " já está em uso por outro aluno.");
                }
            });
            aluno.setEmail(dto.email());
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            aluno.setSenha(passwordEncoder.encode(dto.senha()));
        }
        if (dto.cpf() != null && !dto.cpf().isBlank()) {
            alunoRepository.findByCpf(dto.cpf()).ifPresent(existingAluno -> {
                if (!existingAluno.getId().equals(id)) {
                    throw new AlunoException("O CPF " + dto.cpf() + " já está em uso por outro aluno.");
                }
            });
            aluno.setCpf(dto.cpf());
        }
        if (dto.rg() != null && !dto.rg().isBlank()) {
            aluno.setRg(dto.rg());
        }
        if (dto.endereco() != null && !dto.endereco().isBlank()) {
            aluno.setEndereco(dto.endereco());
        }
        if (dto.curso() != null && !dto.curso().isBlank()) {
            aluno.setCurso(dto.curso());
        }

        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return alunoMapper.toCompletoResponse(alunoAtualizado);
    }
}
