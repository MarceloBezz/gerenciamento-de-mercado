package br.com.fatec.erp.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.fatec.erp.model.dto.dashboardFinanceiro.QuantidadePorProduto;
import br.com.fatec.erp.model.dto.dashboardFinanceiro.VendasPorVendedor;
import br.com.fatec.erp.model.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venda v WHERE v.dataVenda BETWEEN :inicio AND :fim")
    BigDecimal somaTotalPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COALESCE(SUM(vp.quantidade), 0) FROM br.com.fatec.erp.model.venda.VendaProduto vp WHERE vp.venda.dataVenda BETWEEN :inicio AND :fim")
    Long somaQuantidadeVendidaPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.VendasPorVendedor(v.vendedor.nome, COALESCE(SUM(v.total), 0)) " +
           "FROM Venda v GROUP BY v.vendedor.nome ORDER BY SUM(v.total) DESC")
    List<VendasPorVendedor> buscarVendasPorVendedor();

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.VendasPorVendedor(v.vendedor.nome, COALESCE(SUM(v.total), 0)) " +
           "FROM Venda v WHERE MONTH(v.dataVenda) = :mes AND YEAR(v.dataVenda) = :ano " +
           "GROUP BY v.vendedor.nome ORDER BY SUM(v.total) DESC")
    List<VendasPorVendedor> buscarVendasPorVendedorNoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.QuantidadePorProduto(vp.produto.nome, COALESCE(SUM(vp.quantidade), 0)) " +
           "FROM br.com.fatec.erp.model.venda.VendaProduto vp GROUP BY vp.produto.nome ORDER BY SUM(vp.quantidade) DESC")
    List<QuantidadePorProduto> buscarQuantidadeVendidaPorProduto();

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.QuantidadePorProduto(vp.produto.nome, COALESCE(SUM(vp.quantidade), 0)) " +
           "FROM br.com.fatec.erp.model.venda.VendaProduto vp WHERE MONTH(vp.venda.dataVenda) = :mes AND YEAR(vp.venda.dataVenda) = :ano " +
           "GROUP BY vp.produto.nome ORDER BY SUM(vp.quantidade) DESC")
    List<QuantidadePorProduto> buscarQuantidadeVendidaPorProdutoNoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.VendasPorVendedor(v.vendedor.nome, COALESCE(SUM(v.total), 0)) " +
           "FROM Venda v WHERE YEAR(v.dataVenda) = :ano " +
           "GROUP BY v.vendedor.nome ORDER BY SUM(v.total) DESC")
    List<VendasPorVendedor> buscarVendasPorVendedorNoAno(@Param("ano") int ano);

    @Query("SELECT new br.com.fatec.erp.model.dto.dashboardFinanceiro.QuantidadePorProduto(vp.produto.nome, COALESCE(SUM(vp.quantidade), 0)) " +
           "FROM br.com.fatec.erp.model.venda.VendaProduto vp WHERE YEAR(vp.venda.dataVenda) = :ano " +
           "GROUP BY vp.produto.nome ORDER BY SUM(vp.quantidade) DESC")
    List<QuantidadePorProduto> buscarQuantidadeVendidaPorProdutoNoAno(@Param("ano") int ano);
}
