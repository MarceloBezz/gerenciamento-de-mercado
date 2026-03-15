package br.com.fatec.erp.service;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.Lote;
import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.model.dto.LoteDTO;
import br.com.fatec.erp.repository.EstoqueRepository;
import br.com.fatec.erp.repository.LoteRepository;
import br.com.fatec.erp.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoteService {
    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public LoteService(LoteRepository loteRepository, ProdutoRepository produtoRepository, EstoqueRepository estoqueRepository) {
        this.loteRepository = loteRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public Lote cadastrar(LoteDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoID())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        Lote lote = loteRepository.save(new Lote(dto, produto));
        Estoque estoque = estoqueRepository.findByProduto(produto).get();
        estoque.setQuantidade(estoque.getQuantidade() + dto.quantidade());
        return lote;
    }
}
