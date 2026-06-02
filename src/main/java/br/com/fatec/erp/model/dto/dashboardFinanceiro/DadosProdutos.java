package br.com.fatec.erp.model.dto.dashboardFinanceiro;

import java.util.List;

public record DadosProdutos(
        List<String> labels,
        List<Long> quantidade
) {
}
