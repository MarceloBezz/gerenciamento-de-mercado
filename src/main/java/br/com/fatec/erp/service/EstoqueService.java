package br.com.fatec.erp.service;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public List<EstoqueProdutoValidade> listarEstoqueComProdutos() {
        return estoqueRepository.buscarComProdutos();
    }
}
