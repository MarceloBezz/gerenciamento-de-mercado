package br.com.fatec.erp.model.dto.dashboardFinanceiro;

import java.util.List;

public record DadosEntradaSaida(
        List<String> labels,
        List<Double> entradas,
        List<Double> saidas
) {
}
