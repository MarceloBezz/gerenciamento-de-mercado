package br.com.fatec.erp.model;
import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer estoqueMinimo;
    private Double valor;
    private LocalDate dataValidade;
    private Double quantidade;

    public Long getId(){
        return this.id;
    }
    
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public Integer getEstoqueMinimo(){
        return this.estoqueMinimo;
    }

    public void setEsoqueMinimo(int estoqueMinimo){
        this.estoqueMinimo = estoqueMinimo;
    }
    public double getValor(){
        return this.valor;
    }
    public void setValor(double valor){
        this.valor =valor;
    }
    public LocalDate getDataValidade(){
        return this.dataValidade;
    }
    public void setDataValidade(LocalDate dataValidade){
        this.dataValidade = dataValidade;
    }
    public double getQuantidade(){
        return this.quantidade;
    }
    public void setQuantidade(double quantidade){
        this.quantidade = quantidade;
    }
    
}