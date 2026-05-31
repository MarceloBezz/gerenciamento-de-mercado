package br.com.fatec.erp.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.DadosVenda;
import br.com.fatec.erp.model.dto.VendaProdutoDTO;
import br.com.fatec.erp.model.venda.Venda;
import br.com.fatec.erp.model.venda.VendaProduto;
import br.com.fatec.erp.repository.EstoqueRepository;
import br.com.fatec.erp.repository.ProdutoRepository;
import br.com.fatec.erp.repository.UsuarioRepository;
import br.com.fatec.erp.repository.VendaRepository;
import jakarta.transaction.Transactional;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final UsuarioRepository usuarioRepository;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository, EstoqueRepository estoqueRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DadosVenda cadastrar(Usuario vendedor, List<VendaProdutoDTO> dtos) {
        //Usuario vendedor = usuarioRepository.findById(2L).get();
        Venda venda = new Venda(vendedor);

        for (VendaProdutoDTO dto : dtos) {
            Produto produto = produtoRepository.findById(dto.idProduto()).orElseThrow();
            Estoque estoque = estoqueRepository.findByProduto(produto).orElseThrow();

            BigDecimal valor = BigDecimal.valueOf(produto.getValor());
            BigDecimal subtotal = valor.multiply(BigDecimal.valueOf(dto.quantidade()));
            VendaProduto vp = new VendaProduto(venda, produto, dto.quantidade(), valor);

            venda.getItens().add(vp);
            venda.aumentaTotal(subtotal);
            estoque.setQuantidade(estoque.getQuantidade() - dto.quantidade());
        }

        return new DadosVenda(vendaRepository.save(venda));
    }

    public Page<DadosVenda> listarVendas(Pageable pageable) {
        return vendaRepository.findAll(pageable).map(v -> new DadosVenda(v));
    }

    /*
     * public DashboardVendasResumo buscarResumo(){
     * return vendaRepository.buscarResumo();
     * }
     */
}
