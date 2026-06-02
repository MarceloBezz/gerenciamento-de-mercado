package br.com.fatec.erp.model.dto.dashboardFinanceiro;

import java.math.BigDecimal;

public record VendasPorVendedor(String nome, BigDecimal total) {
}
