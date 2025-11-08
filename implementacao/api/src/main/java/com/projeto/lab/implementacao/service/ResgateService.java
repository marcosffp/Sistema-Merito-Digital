package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.dto.ResgateRequest;
import com.projeto.lab.implementacao.dto.ResgateResponse;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.exception.ResgateException;
import com.projeto.lab.implementacao.mapper.ResgateMapper;
import com.projeto.lab.implementacao.mapper.VantagemMapper;
import com.projeto.lab.implementacao.model.Resgate;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.ResgateRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class ResgateService {
    private final ResgateRepository resgateRepository;
    private final AlunoService alunoService;
    private final VantagemService vantagemService;
    private final ResgateMapper resgateMapper;
    private final VantagemMapper vantagemMapper;

    public ResgateResponse cadastrarResgate(ResgateRequest dto) {
        Aluno aluno = alunoService.buscarPorId(dto.alunoId());
        Vantagem vantagem = vantagemService.buscarPorId(dto.vantagemId());

        Resgate resgate = new Resgate();
        resgate.setCupom(gerarCupomAleatorio());
        resgate.setCodigo(String.valueOf(System.currentTimeMillis()));
        resgate.setData(LocalDateTime.now());
        resgate.setValor(vantagem.getCusto());
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);

        Resgate salvo = resgateRepository.save(resgate);

        return resgateMapper.toResponse(salvo);
    }

    private String gerarCupomAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder cupom = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            cupom.append(caracteres.charAt(index));
        }

        return cupom.toString();
    }

    public List<ResgateResponse> listarResgatesPorAluno(Long alunoId) {
        alunoService.buscarPorId(alunoId);

        List<Resgate> resgates = resgateRepository.findAll().stream()
                .filter(resgate -> resgate.getAluno().getId().equals(alunoId))
                .collect(Collectors.toList());

        return resgates.stream()
                .map(resgateMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VantagemResponse mostrarDetalhesVantagemPorResgate(Long resgateId) {
        Resgate resgate = resgateRepository.findById(resgateId)
                .orElseThrow(() -> new ResgateException("Resgate não encontrado"));

        Vantagem vantagem = resgate.getVantagem();
        if (vantagem == null) {
            throw new ResgateException("Nenhuma vantagem associada a este resgate");
        }

        return vantagemMapper.toResponse(vantagem);
    }
}
