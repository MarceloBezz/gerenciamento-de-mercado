package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto(Produto produto);
//    @Query("""
//            SELECT e
//            FROM Estoque e
//            JOIN FETCH e.produto
//            """)
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
    List<EstoqueProdutoValidade> buscarComProdutos();
}
