package br.com.fatec.erp.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.fatec.erp.model.dto.UsuarioAtualizarDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.repository.UsuarioRepository;

@Service // @Service indica que esta classe pertence à camada de regras de negócio.
// O Spring cria e gerencia automaticamente um objeto dessa classe (Bean)
// dentro do seu container (um espaço interno do Spring que armazena e
// gerencia os objetos da aplicação). Assim, outras classes podem receber
// esse objeto através de Injeção de Dependência, sem precisar criá-lo manualmente.
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean validarSenha(String senha) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()]).{8,}$");
        Matcher matcher = pattern.matcher(senha);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public Usuario cadastrar(UsuarioDTO dto) throws Exception {
        Usuario usuario = new Usuario(dto);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuarioRepository.existsByEmailIgnoringCase(usuario.getEmail())) {
            throw new Exception("Usuário já cadastrado!");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) throws Exception {
        return usuarioRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public Usuario buscarPorEmail(String email) throws Exception {
        return usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    public void atualizar(Long id, UsuarioAtualizarDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.email() != null && !dto.email().isBlank()) {
            usuario.setEmail(dto.email());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

