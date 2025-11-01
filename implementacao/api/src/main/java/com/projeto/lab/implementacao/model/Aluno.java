package com.projeto.lab.implementacao.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Aluno extends Participante {
    @Column(unique = true)
    private String rg;
    
    private String endereco;
    
    private String curso;

    @OneToMany(mappedBy = "aluno")
private List<Resgate> resgates;
}
