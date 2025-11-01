package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AlunoUpdateRequest(
    String nome,

    @Email(message = "O email deve ser válido")
    String email,

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    String cpf,

    String rg,

    String endereco,

    String curso
) {}