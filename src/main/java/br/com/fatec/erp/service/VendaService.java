package br.com.fatec.erp.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.DadosVenda;
import br.com.fatec.erp.model.dto.VendaProdutoDTO;
import br.com.fatec.erp.model.venda.Venda;
import br.com.fatec.erp.model.venda.VendaProduto;
import br.com.fatec.erp.repository.ProdutoRepository;
import br.com.fatec.erp.repository.UsuarioRepository;
import br.com.fatec.erp.repository.VendaRepository;
import jakarta.transaction.Transactional;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DadosVenda cadastrar(List<VendaProdutoDTO> dtos) {
        Usuario vendedor = usuarioRepository.findById(1L).get();
        Venda venda = new Venda(vendedor);

        for(VendaProdutoDTO dto : dtos) {
            Produto produto = produtoRepository.findById(dto.idProduto()).orElseThrow();
            BigDecimal valor = BigDecimal.valueOf(produto.getValor());
            BigDecimal subtotal = valor.multiply(BigDecimal.valueOf(dto.quantidade()));
            VendaProduto vp = new VendaProduto(venda, produto, dto.quantidade(), valor);

            venda.getItens().add(vp);
            venda.aumentaTotal(subtotal);
        }

        return new DadosVenda(vendaRepository.save(venda));
    }
}
