package com.projeto.lab.implementacao.controller;

import com.projeto.lab.implementacao.dto.EmpresaCompletaResponse;
import com.projeto.lab.implementacao.dto.EmpresaResumoResponse;
import com.projeto.lab.implementacao.dto.EmpresaRequest;
import com.projeto.lab.implementacao.dto.EmpresaUpdateRequest;
import com.projeto.lab.implementacao.dto.AuthResponse;
import com.projeto.lab.implementacao.model.Empresa;
import com.projeto.lab.implementacao.service.EmpresaService;
import com.projeto.lab.implementacao.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final JwtService jwtService;

    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastrarEmpresa(@Valid @RequestBody EmpresaRequest dto) {
        Empresa empresa = empresaService.cadastrarEmpresa(dto);
        String token = jwtService.generateToken(empresa);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<EmpresaCompletaResponse> obterDadosCompletosEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obterDadosCompletosEmpresa(id));
    }

    @GetMapping("/{id}/resumo")
    public ResponseEntity<EmpresaResumoResponse> obterResumoEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obterResumoEmpresa(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<EmpresaResumoResponse>> listarTodasResumido() {
        return ResponseEntity.ok(empresaService.listarTodasResumido());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaCompletaResponse> editarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateRequest dto) {
        return ResponseEntity.ok(empresaService.updateEmpresa(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}