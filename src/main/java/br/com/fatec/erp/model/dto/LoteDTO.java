package br.com.fatec.erp.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoteDTO(
        @NotNull(message = "Preencha o ID do produto!")
        Long produtoID,
        @NotNull(message = "Preencha a quantidade do lote!")
        @Positive(message = "Quantidade deve ser maior que 0!")
        Integer quantidade,
        @NotNull(message = "Preencha a data de validade!")
        @Future(message = "A data de validade deve ser uma data futura!")
        LocalDate validade
) {
}
