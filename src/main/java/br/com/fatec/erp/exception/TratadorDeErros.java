package br.com.fatec.erp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public String tratarErroGenerico(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Produto não encontrado!");
        return "redirect:/produtos/cadastrar";
    }
}
