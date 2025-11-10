package com.projeto.lab.implementacao.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.projeto.lab.implementacao.dto.EmpresaCompletaResponse;
import com.projeto.lab.implementacao.dto.EmpresaResumoResponse;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.model.Empresa;
import com.projeto.lab.implementacao.model.Vantagem;

@Component
public class EmpresaMapper {

    public EmpresaResumoResponse toResumoResponse(Empresa empresa) {
        List<VantagemResponse> vantagens = empresa.getVantagens() != null
            ? empresa.getVantagens().stream().map(this::toVantagemResponse).collect(Collectors.toList())
            : List.of();

        return new EmpresaResumoResponse(
            empresa.getCnpj(),
            empresa.getNome(),
            vantagens
        );
    }

    public EmpresaCompletaResponse toCompletaResponse(Empresa empresa) {
        List<VantagemResponse> vantagens = empresa.getVantagens() != null
            ? empresa.getVantagens().stream().map(this::toVantagemResponse).collect(Collectors.toList())
            : List.of();

        return new EmpresaCompletaResponse(
            empresa.getId(),
            empresa.getNome(),
            empresa.getEmail(),
            empresa.getCnpj(),
            empresa.getEndereco(),
            vantagens
        );
    }

    private VantagemResponse toVantagemResponse(Vantagem vantagem) {
        return new VantagemResponse(
            vantagem.getId(),
            vantagem.getNome(),
            vantagem.getDescricao(),
            vantagem.getCusto(),
            vantagem.getImagem(),
            vantagem.getEmpresa() != null ? vantagem.getEmpresa().getId() : null
        );
    }
}