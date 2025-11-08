package com.projeto.lab.implementacao.controller;

import com.projeto.lab.implementacao.dto.VantagemRequest;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.service.VantagemService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vantagens")
@AllArgsConstructor
public class VantagemController {
    private final VantagemService vantagemService;

    @PostMapping
    public ResponseEntity<VantagemResponse> cadastrarVantagem(
            @Valid VantagemRequest dto,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        try {
            VantagemResponse vantagem = vantagemService.salvarVantagem(dto, imagem);
            return ResponseEntity.status(HttpStatus.CREATED).body(vantagem);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VantagemResponse> buscarPorId(@PathVariable Long id) {
        VantagemResponse vantagem = vantagemService.buscarVantagemPorId(id);
        return ResponseEntity.ok(vantagem);
    }

    @GetMapping
    public ResponseEntity<List<VantagemResponse>> listarTodas() {
        List<VantagemResponse> vantagens = vantagemService.listarTodas();
        return ResponseEntity.ok(vantagens);
    }

    @GetMapping("/custo-maximo")
    public ResponseEntity<List<VantagemResponse>> listarPorCustoMaximo(@RequestParam Double custoMaximo) {
        List<VantagemResponse> vantagens = vantagemService.listarPorCustoMaximo(custoMaximo);
        return ResponseEntity.ok(vantagens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VantagemResponse> atualizarVantagem(
            @PathVariable Long id,
            @Valid @ModelAttribute VantagemRequest dto,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        try {
            VantagemResponse response = vantagemService.atualizarVantagem(id, dto, imagem);
            return ResponseEntity.ok(response);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}