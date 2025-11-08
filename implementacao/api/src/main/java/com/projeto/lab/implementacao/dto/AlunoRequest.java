package com.projeto.lab.implementacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlunoRequest(
    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank(message = "O CPF é obrigatório")
    String cpf,

    @NotBlank(message = "O RG é obrigatório")
    String rg,

    @NotBlank(message = "O endereço é obrigatório")
    String endereco,

    @NotBlank(message = "O curso é obrigatório")
    String curso,

    @NotBlank(message = "A instituição é obrigatória")
    String instituicao
) {}