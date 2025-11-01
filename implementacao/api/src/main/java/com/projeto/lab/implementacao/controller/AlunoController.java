package com.projeto.lab.implementacao.controller;

import com.projeto.lab.implementacao.dto.AlunoCompletoResponse;
import com.projeto.lab.implementacao.dto.AlunoRequest;
import com.projeto.lab.implementacao.dto.AlunoResumoResponse;
import com.projeto.lab.implementacao.dto.AuthResponse;
import com.projeto.lab.implementacao.dto.AlunoUpdateRequest;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.service.AlunoService;
import com.projeto.lab.implementacao.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final JwtService jwtService;

    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastrarAluno(@Valid @RequestBody AlunoRequest dto) {
        Aluno aluno = alunoService.cadastrarAluno(dto);
        String token = jwtService.generateToken(aluno);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<AlunoCompletoResponse> obterDadosCompletosAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.obterDadosCompletosAluno(id));
    }

    @GetMapping("/{id}/resumo")
    public ResponseEntity<AlunoResumoResponse> obterResumoAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.obterResumoAluno(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<AlunoResumoResponse>> listarTodosResumido() {
        return ResponseEntity.ok(alunoService.listarTodosResumido());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoCompletoResponse> editarAluno(
            @PathVariable Long id,
            @Valid @RequestBody AlunoUpdateRequest dto) {
        return ResponseEntity.ok(alunoService.updateAluno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
