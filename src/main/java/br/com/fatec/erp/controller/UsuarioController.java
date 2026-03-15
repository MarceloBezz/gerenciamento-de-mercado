package br.com.fatec.erp.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public String postMethodName(@Valid @RequestBody UsuarioDTO dto) {
        try {
            usuarioService.cadastrar(dto);
            return  "Cadastrado com sucesso";
        } catch (Exception e) {
            return "erro no cadastro";
        }
    }


}
