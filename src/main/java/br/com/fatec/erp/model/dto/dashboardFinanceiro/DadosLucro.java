package br.com.fatec.erp.model.dto.dashboardFinanceiro;

import java.util.List;

public record DadosLucro(
        List<String> labels,
        List<Double> lucro
) {
}
