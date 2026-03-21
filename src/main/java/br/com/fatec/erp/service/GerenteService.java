package br.com.fatec.erp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.DadosUsuario;
import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class GerenteService {
    private final UsuarioRepository usuarioRepository;

    public GerenteService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DadosUsuario alterarFuncionarios(Long id, UsuarioDTO novosDados) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        if (novosDados.nome() != null && !novosDados.nome().equals("")) {
            usuario.setNome(novosDados.nome());
        }

        if(novosDados.email() != null && !novosDados.email().equals("")){
            usuario.setEmail(novosDados.email());
        }

        usuarioRepository.save(usuario);

        return new DadosUsuario(usuario.getNome(), usuario.getEmail());

        
    }

    public void removerFuncionario(Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            throw new RuntimeException("Usuário inexistente");
        }

        usuarioRepository.deleteById(id);
    }
}
