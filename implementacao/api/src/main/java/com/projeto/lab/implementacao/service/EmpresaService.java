package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.dto.EmpresaCompletaResponse;
import com.projeto.lab.implementacao.dto.EmpresaResumoResponse;
import com.projeto.lab.implementacao.dto.EmpresaRequest;
import com.projeto.lab.implementacao.dto.EmpresaUpdateRequest;
import com.projeto.lab.implementacao.exception.EmpresaException;
import com.projeto.lab.implementacao.mapper.EmpresaMapper;
import com.projeto.lab.implementacao.model.Empresa;
import com.projeto.lab.implementacao.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final PasswordEncoder passwordEncoder;

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EmpresaException("Empresa com ID " + id + " não encontrada"));
    }

    public Empresa buscarPorEmail(String email) {
        return empresaRepository.findByEmail(email)
                .orElseThrow(() -> new EmpresaException("Empresa com email " + email + " não encontrada"));
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new EmpresaException("Empresa com CNPJ " + cnpj + " não encontrada"));
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public List<EmpresaResumoResponse> listarTodasResumido() {
        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream()
                .map(empresaMapper::toResumoResponse)
                .toList();
    }

    public Empresa atualizar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public void deletar(Long id) {
        empresaRepository.deleteById(id);
    }

    @Transactional
    public Empresa cadastrarEmpresa(EmpresaRequest dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.nome());
        empresa.setEmail(dto.email());
        empresa.setSenha(passwordEncoder.encode(dto.senha()));
        empresa.setCnpj(dto.cnpj());
        empresa.setEndereco(dto.endereco());

        return empresaRepository.save(empresa);
    }

    public EmpresaResumoResponse obterResumoEmpresa(Long id) {
        Empresa empresa = buscarPorId(id);
        return empresaMapper.toResumoResponse(empresa);
    }

    public EmpresaCompletaResponse obterDadosCompletosEmpresa(Long id) {
        Empresa empresa = buscarPorId(id);
        return empresaMapper.toCompletaResponse(empresa);
    }

    @Transactional
    public EmpresaCompletaResponse updateEmpresa(Long id, EmpresaUpdateRequest dto) {
        Empresa empresa = buscarPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            empresa.setNome(dto.nome());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            empresaRepository.findByEmail(dto.email()).ifPresent(existingEmpresa -> {
                if (!existingEmpresa.getId().equals(id)) {
                    throw new EmpresaException("O email " + dto.email() + " já está em uso por outra empresa.");
                }
            });
            empresa.setEmail(dto.email());
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            empresa.setSenha(passwordEncoder.encode(dto.senha()));
        }
        if (dto.cnpj() != null && !dto.cnpj().isBlank()) {
            empresaRepository.findByCnpj(dto.cnpj()).ifPresent(existingEmpresa -> {
                if (!existingEmpresa.getId().equals(id)) {
                    throw new EmpresaException("O CNPJ " + dto.cnpj() + " já está em uso por outra empresa.");
                }
            });
            empresa.setCnpj(dto.cnpj());
        }
        if (dto.endereco() != null && !dto.endereco().isBlank()) {
            empresa.setEndereco(dto.endereco());
        }

        Empresa empresaAtualizada = empresaRepository.save(empresa);
        return empresaMapper.toCompletaResponse(empresaAtualizada);
    }
}
