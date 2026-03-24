package br.com.fatec.erp.model.venda;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class VendaProdutoId implements Serializable {

    @Column(name = "venda_id")
    private Long vendaId;

    @Column(name = "produto_id")
    private Long produtoId;

    public VendaProdutoId() {}

    public VendaProdutoId(Long vendaId, Long produtoId) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public Long getProdutoId() {
        return produtoId;
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

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}