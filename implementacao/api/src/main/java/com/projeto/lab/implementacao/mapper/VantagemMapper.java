package com.projeto.lab.implementacao.mapper;

import org.springframework.stereotype.Component;

import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.model.Vantagem;

@Component
public class VantagemMapper {

    public VantagemResponse toResponse(Vantagem vantagem) {
        return new VantagemResponse(
            vantagem.getId(),
            vantagem.getNome(),
            vantagem.getDescricao(),
            vantagem.getCusto(),
            vantagem.getImagem()
        );
    }
}