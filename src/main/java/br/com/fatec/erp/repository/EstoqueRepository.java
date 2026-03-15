package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto(Produto produto);
}
