package com.projeto.lab.implementacao.model;

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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Participante extends Usuario {
    @Column(nullable = false)
    private Double saldoMoedas = 0.0;
    
    @Column(unique = true, nullable = false)
    private String cpf;
}
