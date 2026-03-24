package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.venda.VendaProduto;
import br.com.fatec.erp.model.venda.VendaProdutoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaProdutoRepository extends JpaRepository<VendaProduto, VendaProdutoId> {
}
