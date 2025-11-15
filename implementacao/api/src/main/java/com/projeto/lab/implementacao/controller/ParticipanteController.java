package com.projeto.lab.implementacao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.lab.implementacao.dto.TransacaoAgrupadaResponse;
import com.projeto.lab.implementacao.service.ParticipanteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/participantes")
@AllArgsConstructor
public class ParticipanteController {
    private final ParticipanteService participanteService;


    @GetMapping("/{id}/saldo")
    public double consultarSaldo(@PathVariable Long id) {
        return participanteService.consultarSaldo(id);
    }

    @GetMapping("/{id}/extrato")
    public TransacaoAgrupadaResponse obterExtrato(@PathVariable Long id) {
        return participanteService.consultarExtrato(id);
    }

}
