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
public abstract class Transacao implements Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String codigo;
    
    @Column(nullable = false)
    private LocalDateTime data;
    
    @Column(nullable = false)
    private Double valor;
    
    @Column(nullable = false)
    private String status;
}
