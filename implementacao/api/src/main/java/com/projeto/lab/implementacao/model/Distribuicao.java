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
public class Distribuicao extends Transacao {
    @Column(nullable = false, length = 500)
    private String motivo;
    
    @Override
    public void enviarNotificacao() {
        // Implementação da notificação de distribuição
    }
}
