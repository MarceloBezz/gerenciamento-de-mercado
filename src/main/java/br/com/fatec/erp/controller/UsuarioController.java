package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.UsuarioAtualizarDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.service.UsuarioService;
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

    

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id) throws Exception {
            usuarioService.buscarPorId(id);
            return "Funcionario encontrado";
    }

    @GetMapping("/email")
    public String buscarPorEmail(@RequestParam String email) {
        try {
            usuarioService.buscarPorEmail(email);
            return "Funcionario encontrado";
        } catch (Exception e) {
            return  "Funcionario não encontrado";
        }
    }

    @GetMapping("/listar")
    public String listarFuncionario() {
        usuarioService.listarUsuario();
        return "funcionarios";
    }

    @PostMapping("/atualizar")
    public String atualizaUsuario(@RequestBody UsuarioAtualizarDTO usuarioAtualizarDTO, Model model) throws Exception {

        Usuario usuario = usuarioService.atualizar(usuarioAtualizarDTO);

         return "meus-dados";
    }

}
