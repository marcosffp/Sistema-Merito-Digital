package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
    Optional<Aluno> findByCpf(String cpf);
    Optional<Aluno> findByRg(String rg);
}
