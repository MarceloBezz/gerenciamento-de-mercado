package br.com.fatec.erp.model.dto.dashboardFinanceiro;

import java.util.List;

public record DadosVendedores(
        List<String> labels,
        List<Double> vendas
) {
}
