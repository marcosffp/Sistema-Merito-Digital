package com.projeto.lab.implementacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projeto.lab.implementacao.dto.DistribuicaoRequest;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.service.DistribuicaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/distribuicoes")
@RequiredArgsConstructor
public class DistribuicaoController {

    private final DistribuicaoService distribuicaoService;

    @PostMapping
    public ResponseEntity<Distribuicao> cadastrarDistribuicao(@Valid @RequestBody DistribuicaoRequest request) {
        try {
            Distribuicao distribuicao = distribuicaoService.cadastrarDistribuicao(
                request.professorId(),
                request.alunoId(),
                request.valor(),
                request.motivo()
            );
            return ResponseEntity.ok(distribuicao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar distribuição: " + e.getMessage(), e);
        }
    }
}
