package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.model.dto.DashboardEstoqueResumo;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto(Produto produto);

    @Query("""
            SELECT new br.com.fatec.erp.model.dto.EstoqueProdutoValidade(
                e,
                MIN(l.validade)
            )
            FROM Estoque e
            JOIN e.produto p
            LEFT JOIN Lote l ON l.produto = p
            GROUP BY e
            """)
    Page<EstoqueProdutoValidade> buscarComProdutos(Pageable pageable);

    @Query(value = """
            SELECT
                COUNT(e.produto_id) AS total_produtos,
                SUM(CASE WHEN e.quantidade <= e.quantidade_minima THEN 1 ELSE 0 END) AS estoque_baixo,
                SUM(CASE WHEN l.min_validade < CURRENT_DATE THEN 1 ELSE 0 END) AS vencidos,
                SUM(e.quantidade * p.valor) AS valor_total
            FROM estoque e
            JOIN produtos p ON p.id = e.produto_id
            LEFT JOIN (
                SELECT produto_id, MIN(validade) AS min_validade
                FROM lotes
                GROUP BY produto_id
            ) l ON l.produto_id = e.produto_id;
            """, nativeQuery = true)
    DashboardEstoqueResumo buscarResumo();
}
