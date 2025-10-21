package com.projeto.lab.implementacao.repository;

import com.projeto.lab.implementacao.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);
    Optional<Professor> findByCpf(String cpf);
}
