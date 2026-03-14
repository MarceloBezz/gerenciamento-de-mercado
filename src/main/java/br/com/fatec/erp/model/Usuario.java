package br.com.fatec.erp.model;

public class Usuario {
    private Long id;
    private String email;
    private String senha;
    private Cargo cargo;

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
