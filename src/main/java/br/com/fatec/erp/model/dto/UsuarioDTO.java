package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.Cargo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotBlank String nome ,
        @Email String email,
        @NotBlank String senha,
        @NotNull Cargo cargo) {

}
