package br.com.fatec.erp.exception;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratadorDeErros {

//    @ExceptionHandler(UsuarioJaCadastradoException.class)
//    public String tratarErroGenerico(BindingResult result) {
//        result.rejectValue("email", "email", "Email já cadastrado!");
//        return "cadastro-funcionario";
//    }
}
