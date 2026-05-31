package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
