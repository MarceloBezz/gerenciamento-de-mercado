package br.com.fatec.erp.model.dto;

import java.math.BigDecimal;

public record VendaProdutoDTO(
        Long idProduto,
        Integer quantidade
) {
}
