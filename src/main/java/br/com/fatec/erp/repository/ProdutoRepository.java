package br.com.fatec.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fatec.erp.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}