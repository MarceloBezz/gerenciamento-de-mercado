package br.com.fatec.erp.model.dto;

import java.math.BigDecimal;

public record DashboardEstoqueResumo(
        Long totalProdutos,
        Long estoqueAbaixo,
        Long vencidos,
        BigDecimal valorTotal
) {
}
