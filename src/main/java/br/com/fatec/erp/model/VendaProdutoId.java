package br.com.fatec.erp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Venda
@Entity
@Table(name = "vendas")
class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendedor_id") // igual à query
    private Usuario vendedor;

    @Column(name = "data_venda") // igual à query
    private LocalDate dataVenda;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(mappedBy = "venda")
    private List<VendaProduto> itens;
}

// Venda Produto
@Entity
@Table(name = "venda_produto")
class VendaProduto {

    @EmbeddedId
    private VendaProdutoId id;

    @ManyToOne
    @MapsId("vendaId")
    @JoinColumn(name = "venda_id") // igual à query
    private Venda venda;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id") // igual à query
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valor")
    private BigDecimal valor;
}

// Id composto
@Embeddable
public class VendaProdutoId implements java.io.Serializable {

    @Column(name = "venda_id")
    private Long vendaId;

    @Column(name = "produto_id")
    private Long produtoId;

    public VendaProdutoId() {}

    public VendaProdutoId(Long vendaId, Long produtoId) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VendaProdutoId)) return false;
        VendaProdutoId that = (VendaProdutoId) o;
        return vendaId.equals(that.vendaId) &&
               produtoId.equals(that.produtoId);
    }

    @Override
    public int hashCode() {
        return vendaId.hashCode() + produtoId.hashCode();
    }
}