package br.com.fatec.erp.model;

import br.com.fatec.erp.model.dto.LoteDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lotes")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    private Integer quantidade;
    private LocalDate validade;

    public Lote() {
    }

    public Lote(LoteDTO dto, Produto produto) {
        this.produto = produto;
        this.quantidade = dto.quantidade();
        this.validade = dto.validade();
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public LocalDate getValidade() {
        return validade;
    }
}
