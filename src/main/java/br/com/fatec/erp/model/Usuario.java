package br.com.fatec.erp.model;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    public Usuario(String email, String senha, Cargo cargo) {
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
    }

    public Usuario() {}

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
}
