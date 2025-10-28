package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Resgate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResgateRepository extends JpaRepository<Resgate, Long> {
    Optional<Resgate> findByCupom(String cupom);
    Optional<Resgate> findByCodigo(String codigo);
}
