package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.Estoque;

import java.time.LocalDate;

public record EstoqueProdutoValidade(
        Estoque estoque,
        LocalDate validade
) {
}
