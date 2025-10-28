package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Empresa;
import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    private final EmpresaRepository empresaRepository;

    public Empresa criar(Empresa empresa) {
        if (empresa.getVantagens() == null) {
            empresa.setVantagens(new ArrayList<>());
        }
        return empresaRepository.save(empresa);
    }

    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepository.findById(id);
    }

    public Optional<Empresa> buscarPorEmail(String email) {
        return empresaRepository.findByEmail(email);
    }

    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa atualizar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public void deletar(Long id) {
        empresaRepository.deleteById(id);
    }

    @Transactional
    public Empresa adicionarVantagem(Long empresaId, Vantagem vantagem) {
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        empresa.getVantagens().add(vantagem);
        return empresaRepository.save(empresa);
    }

    public List<Vantagem> listarVantagens(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        return empresa.getVantagens();
    }
}
