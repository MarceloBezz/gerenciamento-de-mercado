package br.com.fatec.erp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.repository.ProdutoRepository;

@Service
public class ProdutoService {
    private ProdutoRepository  produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }
    public Produto salvarProduto(Produto produto){
        return produtoRepository.save(produto);
    }
    public void deletarProduto(Long id){
        produtoRepository.deleteById(id);
    }
}

