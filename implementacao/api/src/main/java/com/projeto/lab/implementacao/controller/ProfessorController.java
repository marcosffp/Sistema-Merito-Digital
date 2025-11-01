package com.projeto.lab.implementacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.lab.implementacao.dto.ProfessorCompletoResponse;
import com.projeto.lab.implementacao.dto.ProfessorResumoResponse;
import com.projeto.lab.implementacao.dto.ProfessorUpdateRequest;
import com.projeto.lab.implementacao.service.ProfessorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorCompletoResponse> obterDadosCompletos(@PathVariable Long id) {
        ProfessorCompletoResponse response = professorService.obterDadosCompletosProfessor(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResumoResponse>> listarTodosResumido() {
        List<ProfessorResumoResponse> response = professorService.listarTodosResumido();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorCompletoResponse> atualizarProfessor(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorUpdateRequest dto) {
        ProfessorCompletoResponse response = professorService.updateProfessor(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}