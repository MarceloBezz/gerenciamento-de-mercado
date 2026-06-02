package br.com.fatec.erp.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.dto.dashboardFinanceiro.DadosEntradaSaida;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.DadosLucro;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.DadosProdutos;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.DadosVendedores;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.QuantidadePorProduto;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.VendasPorVendedor;
import br.com.fatec.erp.repository.VendaRepository;

@Service
public class FinanceiroService {
    private static final List<String> SEMANAS = List.of("Semana 1", "Semana 2", "Semana 3", "Semana 4");
    private final VendaRepository vendaRepository;

    public FinanceiroService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public DadosEntradaSaida buscarEntradasESaidas(Integer mes, Integer ano) {
        int anoFiltrado = (ano == null) ? LocalDate.now().getYear() : ano;

        // Se mes for null -> retornar dados mensais do ano inteiro (12 meses)
        if (mes == null) {
            List<String> meses = new ArrayList<>();
            List<Double> entradas = new ArrayList<>();
            List<Double> saidas = new ArrayList<>();

            for (int m = 1; m <= 12; m++) {
                LocalDate primeiroDia = LocalDate.of(anoFiltrado, m, 1);
                LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
                BigDecimal total = vendaRepository.somaTotalPorPeriodo(primeiroDia, ultimoDia);
                Long quantidade = vendaRepository.somaQuantidadeVendidaPorPeriodo(primeiroDia, ultimoDia);
                entradas.add(total.doubleValue());
                saidas.add(quantidade.doubleValue());
                meses.add(primeiroDia.getMonth().name().substring(0,1).toUpperCase() + primeiroDia.getMonth().name().substring(1).toLowerCase());
            }

            return new DadosEntradaSaida(meses, entradas, saidas);
        }

        int mesFiltrado = resolveMes(mes);
        LocalDate primeiroDia = LocalDate.of(anoFiltrado, mesFiltrado, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        List<Double> entradas = new ArrayList<>();
        List<Double> saidas = new ArrayList<>();
        LocalDate periodoInicio = primeiroDia;

        for (int i = 0; i < SEMANAS.size(); i++) {
            LocalDate periodoFim = (i < 3) ? periodoInicio.plusDays(6) : ultimoDia;
            if (periodoFim.isAfter(ultimoDia)) {
                periodoFim = ultimoDia;
            }

            BigDecimal total = vendaRepository.somaTotalPorPeriodo(periodoInicio, periodoFim);
            Long quantidade = vendaRepository.somaQuantidadeVendidaPorPeriodo(periodoInicio, periodoFim);
            entradas.add(total.doubleValue());
            saidas.add(quantidade.doubleValue());

            periodoInicio = periodoFim.plusDays(1);
            if (periodoInicio.isAfter(ultimoDia)) {
                periodoInicio = ultimoDia;
            }
        }

        return new DadosEntradaSaida(SEMANAS, entradas, saidas);
    }

    public DadosVendedores buscarVendasPorVendedor(Integer mes, Integer ano) {
        int anoFiltrado = (ano == null) ? LocalDate.now().getYear() : ano;
        int mesFiltrado = resolveMes(mes);
        List<VendasPorVendedor> resumo;

        if (mes == null) {
            resumo = vendaRepository.buscarVendasPorVendedorNoAno(anoFiltrado);
        } else {
            resumo = vendaRepository.buscarVendasPorVendedorNoMes(mesFiltrado, anoFiltrado);
        }

        List<String> labels = resumo.stream().map(VendasPorVendedor::nome).toList();
        List<Double> vendas = resumo.stream().map(item -> item.total().doubleValue()).toList();
        return new DadosVendedores(labels, vendas);
    }

    public DadosProdutos buscarQuantidadePorProduto(Integer mes, Integer ano) {
        int anoFiltrado = (ano == null) ? LocalDate.now().getYear() : ano;
        int mesFiltrado = resolveMes(mes);
        List<QuantidadePorProduto> resumo;

        if (mes == null) {
            resumo = vendaRepository.buscarQuantidadeVendidaPorProdutoNoAno(anoFiltrado);
        } else {
            resumo = vendaRepository.buscarQuantidadeVendidaPorProdutoNoMes(mesFiltrado, anoFiltrado);
        }

        List<String> labels = resumo.stream().map(QuantidadePorProduto::nome).toList();
        List<Long> quantidade = resumo.stream().map(QuantidadePorProduto::quantidade).toList();
        return new DadosProdutos(labels, quantidade);
    }

    public DadosLucro buscarLucroSemanal(Integer mes, Integer ano) {
        int anoFiltrado = (ano == null) ? LocalDate.now().getYear() : ano;

        // Se mes for null -> retornar lucro por mês para o ano inteiro
        if (mes == null) {
            List<String> meses = new ArrayList<>();
            List<Double> lucro = new ArrayList<>();
            for (int m = 1; m <= 12; m++) {
                LocalDate primeiroDia = LocalDate.of(anoFiltrado, m, 1);
                LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
                BigDecimal total = vendaRepository.somaTotalPorPeriodo(primeiroDia, ultimoDia);
                lucro.add(total.doubleValue());
                meses.add(primeiroDia.getMonth().name().substring(0,1).toUpperCase() + primeiroDia.getMonth().name().substring(1).toLowerCase());
            }
            return new DadosLucro(meses, lucro);
        }

        int mesFiltrado = resolveMes(mes);
        LocalDate primeiroDia = LocalDate.of(anoFiltrado, mesFiltrado, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        List<Double> lucro = new ArrayList<>();
        LocalDate periodoInicio = primeiroDia;

        for (int i = 0; i < SEMANAS.size(); i++) {
            LocalDate periodoFim = (i < 3) ? periodoInicio.plusDays(6) : ultimoDia;
            if (periodoFim.isAfter(ultimoDia)) {
                periodoFim = ultimoDia;
            }

            BigDecimal total = vendaRepository.somaTotalPorPeriodo(periodoInicio, periodoFim);
            lucro.add(total.doubleValue());

            periodoInicio = periodoFim.plusDays(1);
            if (periodoInicio.isAfter(ultimoDia)) {
                periodoInicio = ultimoDia;
            }
        }

        return new DadosLucro(SEMANAS, lucro);
    }

    private int resolveMes(Integer mes) {
        if (mes == null || mes < 1 || mes > 12) {
            return LocalDate.now().getMonthValue();
        }
        return mes;
    }

    private LocalDate inicioDasQuatroSemanas() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(3);
    }
}
