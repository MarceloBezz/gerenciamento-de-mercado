package br.com.fatec.erp.model;

import jakarta.persistence.*;

@Entity
public class Estoque {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "produto_id")
    private Produto produto;
    private Integer quantidade;
    private Integer quantidadeMinima;

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Estoque() {
    }

    public Estoque(Produto produto, Integer quantidade, Integer quantidadeMinima) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}
