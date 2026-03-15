package br.com.fatec.erp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoDTO(
        @NotBlank(message = "Preencha o nome do produto!")
        String nome,
        @NotNull(message = "Preencha o valor do produto!")
        @Positive(message = "O valor do produto deve ser maior que zero!")
        Double valor,
        @NotBlank(message = "Preencha a descrição do produto!")
        String descricao
) {
}
