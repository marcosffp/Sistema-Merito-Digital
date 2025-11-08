package com.projeto.lab.implementacao.controller;

import com.projeto.lab.implementacao.dto.ResgateRequest;
import com.projeto.lab.implementacao.dto.ResgateResponse;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.service.ResgateService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resgates")
@AllArgsConstructor
public class ResgateController {

    private final ResgateService resgateService;

    @PostMapping
    public ResponseEntity<ResgateResponse> cadastrarResgate(@Valid @RequestBody ResgateRequest resgateRequest) {
        ResgateResponse resgateResponse = resgateService.cadastrarResgate(resgateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resgateResponse);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ResgateResponse>> listarResgatesPorAluno(@PathVariable Long alunoId) {
        List<ResgateResponse> resgates = resgateService.listarResgatesPorAluno(alunoId);
        return ResponseEntity.ok(resgates);
    }

    @GetMapping("/{resgateId}/vantagem")
    public ResponseEntity<VantagemResponse> mostrarDetalhesVantagemPorResgate(@PathVariable Long resgateId) {
        VantagemResponse vantagemResponse = resgateService.mostrarDetalhesVantagemPorResgate(resgateId);
        return ResponseEntity.ok(vantagemResponse);
    }
}