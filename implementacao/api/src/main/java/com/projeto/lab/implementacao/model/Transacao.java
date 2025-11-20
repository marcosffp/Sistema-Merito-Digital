package com.projeto.lab.implementacao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String codigo;
    
    @Column(nullable = false)
    private LocalDateTime data;
    
    @Column(nullable = false)
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "pagador_id", nullable = true)
    private Participante pagador;

    @ManyToOne
    @JoinColumn(name = "recebedor_id", nullable = true)
    private Participante recebedor;


}
