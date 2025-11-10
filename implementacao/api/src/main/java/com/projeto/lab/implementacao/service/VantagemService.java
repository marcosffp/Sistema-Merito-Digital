package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.dto.VantagemRequest;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.exception.VantagemException;
import com.projeto.lab.implementacao.mapper.VantagemMapper;
import com.projeto.lab.implementacao.model.Empresa;
import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.VantagemRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VantagemService {
    private final VantagemRepository vantagemRepository;
    private final ImagemService imageService;
    private final EmpresaService empresaService;
    private final VantagemMapper vantagemMapper;

    public VantagemResponse salvarVantagem(VantagemRequest dto, MultipartFile imagem) throws IOException {
        Empresa empresa = empresaService.buscarPorId(dto.empresaId());
        if (empresa == null) {
            throw new VantagemException("Empresa não encontrada");
        }

        Vantagem vantagem = new Vantagem();
        vantagem.setNome(dto.nome());
        vantagem.setDescricao(dto.descricao());
        vantagem.setCusto(dto.custo());
        vantagem.setEmpresa(empresa);
        if (imagem != null && !imagem.isEmpty()) {
            String imageUrl = imageService.uploadImage(imagem, "vantagens");
            vantagem.setImagem(imageUrl);
        }

        Vantagem savedVantagem = vantagemRepository.save(vantagem);
        return vantagemMapper.toResponse(savedVantagem);
    }

    public Vantagem buscarPorId(Long id) {
        return vantagemRepository.findById(id).orElseThrow(() -> new VantagemException("Vantagem não encontrada"));
    }

    public VantagemResponse buscarVantagemPorId(Long id) {
        Vantagem vantagem = buscarPorId(id);
        return vantagemMapper.toResponse(vantagem);
    }

    public List<VantagemResponse> listarTodas() {
        return vantagemRepository.findAll().stream()
                .map(vantagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<VantagemResponse> listarPorCustoMaximo(Double custoMaximo) {
        return vantagemRepository.findByCustoLessThanEqual(custoMaximo).stream()
                .map(vantagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VantagemResponse atualizarVantagem(Long id, VantagemRequest dto, MultipartFile imagem) throws IOException {
        Vantagem vantagemExistente = vantagemRepository.findById(id)
                .orElseThrow(() -> new VantagemException("Vantagem não encontrada"));

        Empresa empresa = empresaService.buscarPorId(dto.empresaId());
        if (empresa == null) {
            throw new VantagemException("Empresa associada à vantagem não encontrada");
        }

        vantagemExistente.setNome(dto.nome());
        vantagemExistente.setDescricao(dto.descricao());
        vantagemExistente.setCusto(dto.custo());
        vantagemExistente.setEmpresa(empresa);

        if (imagem != null && !imagem.isEmpty()) {
            String imageUrl = imageService.uploadImage(imagem, "vantagens");
            vantagemExistente.setImagem(imageUrl);
        }

        Vantagem updatedVantagem = vantagemRepository.save(vantagemExistente);

        return vantagemMapper.toResponse(updatedVantagem);
    }

    public void deletarVantagem(Long id) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new VantagemException("Vantagem não encontrada"));
        vantagemRepository.delete(vantagem);
    }
}
