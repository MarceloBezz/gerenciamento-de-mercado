package br.com.fatec.erp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.repository.UsuarioRepository;

@Service
public class GerenteService {
    private final UsuarioRepository usuarioRepository;

    public GerenteService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    /*public Usuario alterarFuncionarios(Long id, UsuarioDTO novosDados){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isEmpty()){
            throw new RuntimeException("O usuário inexistente");
        }

        return
    }*/

    public void removerFuncionario(Long id){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isEmpty()){
            throw new RuntimeException("Usuário inexistente");
        }

        usuarioRepository.deleteById(id);
    }

}
