package br.com.fatec.erp.model.venda;

import br.com.fatec.erp.model.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id") // igual à query
    private Usuario vendedor;

    private LocalDate dataVenda;
    private BigDecimal total;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaProduto> itens = new ArrayList<>();

    public Venda() {
    }

    public Venda(Usuario vendedor) {
        this.vendedor = vendedor;
        this.dataVenda = LocalDate.now();
        this.total = new BigDecimal(0);
    }

    public void aumentaTotal(BigDecimal valor) {
        this.total = this.total.add(valor);
    }


    public Long getId() {
        return id;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<VendaProduto> getItens() {
        return itens;
    }
}

