package br.com.fatec.erp.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service // @Service indica que esta classe pertence à camada de regras de negócio.
// O Spring cria e gerencia automaticamente um objeto dessa classe (Bean)
// dentro do seu container (um espaço interno do Spring que armazena e
// gerencia os objetos da aplicação). Assim, outras classes podem receber
// esse objeto através de Injeção de Dependência, sem precisar criá-lo manualmente.
public class UsuarioService {

    private void validarSenha(String senha) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()]).{8,}$");
        Matcher matcher = pattern.matcher(senha); 
    }
}
