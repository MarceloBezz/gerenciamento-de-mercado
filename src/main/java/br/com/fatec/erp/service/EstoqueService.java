package br.com.fatec.erp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.dto.DashboardEstoqueResumo;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.repository.EstoqueRepository;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Page<EstoqueProdutoValidade> listarEstoqueComProdutos(Pageable pageable, String filtroProduto, String filtroStatus) {
        return estoqueRepository.buscarComProdutos(pageable, filtroProduto, filtroStatus);
    }

    public DashboardEstoqueResumo buscarResumo() {
        return estoqueRepository.buscarResumo();
    }
}
