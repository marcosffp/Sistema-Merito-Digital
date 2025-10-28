package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {
    Optional<Instituicao> findByCnpj(String cnpj);
    Optional<Instituicao> findByNome(String nome);
}
