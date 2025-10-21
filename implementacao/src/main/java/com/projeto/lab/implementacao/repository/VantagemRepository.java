package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    List<Vantagem> findByCustoLessThanEqual(Double custo);
}
