package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Distribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DistribuicaoRepository extends JpaRepository<Distribuicao, Long> {
    Optional<Distribuicao> findByCodigo(String codigo);
}
