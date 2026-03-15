package br.com.fatec.erp.service;

import java.util.List;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.dto.ProdutoDTO;
import br.com.fatec.erp.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.repository.ProdutoRepository;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepository, EstoqueRepository estoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto salvarProduto(ProdutoDTO dto) {
        Produto produto = new Produto(dto.nome(), dto.valor(), dto.descricao());
        Estoque estoque = new Estoque(produto, 0, dto.quantidadeMinimaEstoque());
        estoqueRepository.save(estoque);
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}

