package br.com.fatec.erp.model.venda;

import br.com.fatec.erp.model.Produto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "venda_produto")
public class VendaProduto {
    @EmbeddedId
    private VendaProdutoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendaId")
    @JoinColumn(name = "venda_id") // igual à query
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id") // igual à query
    private Produto produto;

    private Integer quantidade;
    private BigDecimal valor;

    public VendaProduto() {
    }

    public VendaProduto(Venda venda, Produto produto, Integer quantidade, BigDecimal valor) {
        this.id = new VendaProdutoId();
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public VendaProdutoId getId() {
        return id;
    }

    public Venda getVenda() {
        return venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}