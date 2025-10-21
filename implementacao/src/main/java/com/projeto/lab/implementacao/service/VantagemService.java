package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.VantagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VantagemService {
    private final VantagemRepository vantagemRepository;

    public Vantagem criar(Vantagem vantagem) {
        return vantagemRepository.save(vantagem);
    }

    public Optional<Vantagem> buscarPorId(Long id) {
        return vantagemRepository.findById(id);
    }

    public List<Vantagem> listarTodas() {
        return vantagemRepository.findAll();
    }

    public List<Vantagem> listarPorCustoMaximo(Double custoMaximo) {
        return vantagemRepository.findByCustoLessThanEqual(custoMaximo);
    }

    public Vantagem atualizar(Vantagem vantagem) {
        return vantagemRepository.save(vantagem);
    }

    public void deletar(Long id) {
        vantagemRepository.deleteById(id);
    }
}
