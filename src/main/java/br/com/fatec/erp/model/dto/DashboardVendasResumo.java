package br.com.fatec.erp.model.dto;

import java.math.BigDecimal;

public record DashboardVendasResumo(
        Long totalVendas,
        BigDecimal valorTotal
) {
}
