package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.Cargo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotBlank(message = "Preencha o nome do usuário!")
        String nome,
        @Email(message = "Formato de email inválido!")
        @NotBlank(message = "Preencha o email do usuário!")
        String email,
        @NotBlank(message = "Preencha a senha do usuário!")
        String senha,
        @NotNull(message = "Preencha o cargo do usuário!")
        Cargo cargo) {

}
