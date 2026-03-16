package br.com.fatec.erp.model;

import br.com.fatec.erp.model.dto.UsuarioDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    public Usuario(UsuarioDTO dto) {
        this.email = dto.email();
        this.nome = dto.nome();
        this.senha = dto.senha();
        this.cargo = dto.cargo();
    }

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
