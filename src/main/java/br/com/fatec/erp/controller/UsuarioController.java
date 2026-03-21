package br.com.fatec.erp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/funcionarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public String cadastrarFuncionario(@Valid UsuarioDTO usuarioDTO, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "cadastro-funcionario";
        }

        try {
            usuarioService.cadastrar(usuarioDTO);
            redirect.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/funcionarios/cadastrar";
        } catch (Exception e) {
            result.rejectValue("email", "email", "Email já cadastrado!");
            return "cadastro-funcionario";
        }
    }


}
