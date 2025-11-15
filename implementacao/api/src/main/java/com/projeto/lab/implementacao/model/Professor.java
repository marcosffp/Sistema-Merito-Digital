package com.projeto.lab.implementacao.model;

import java.time.LocalDate;

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
public class Professor extends Participante {
    private String departamento;

    private LocalDate ultimaAtualizacaoSaldo;
}
