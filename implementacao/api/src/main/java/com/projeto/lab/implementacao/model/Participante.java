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
public abstract class Participante extends Usuario {
    @Column(nullable = false)
    private Double saldoMoedas;
    
    @Column(unique = true, nullable = false)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false) 
    private Instituicao instituicao;

    @OneToMany(mappedBy = "pagador", cascade = CascadeType.ALL)
    private List<Transacao> transacoesComoPagador;

    @OneToMany(mappedBy = "recebedor", cascade = CascadeType.ALL)
    private List<Transacao> transacoesComoRecebedor;
}
