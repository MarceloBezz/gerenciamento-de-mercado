package br.com.fatec.erp.service;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.dto.DashboardEstoqueResumo;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.repository.EstoqueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Page<EstoqueProdutoValidade> listarEstoqueComProdutos(Pageable pageable) {
        return estoqueRepository.buscarComProdutos(pageable);
    }

    public DashboardEstoqueResumo buscarResumo() {
        return estoqueRepository.buscarResumo();
    }
}
