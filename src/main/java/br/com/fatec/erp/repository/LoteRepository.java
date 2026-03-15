package br.com.fatec.erp.repository;

import br.com.fatec.erp.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long> {
}
