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

import java.util.List;


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

    @GetMapping("/editar/{id}")
    public String editarFuncionario(@PathVariable Long id, Model model,
                                    @AuthenticationPrincipal UsuarioSecurity usuarioSecurity) throws Exception {

        Usuario usuario = usuarioService.buscarPorId(id);

        UsuarioDTO dto = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                null,
                usuario.getCargo()
        );

        model.addAttribute("usuario", usuarioSecurity.getUsuario());
        model.addAttribute("usuarioDTO", dto);

        return "cadastro-funcionario";
    }

    @GetMapping
    public String listarFuncionarios(Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioSecurity) {
        model.addAttribute("usuario", usuarioSecurity.getUsuario());
        List<Usuario> funcionarios = usuarioService.listarUsuario();

        model.addAttribute("funcionarios", funcionarios);

        return "gerenciar-funcionarios";
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
            return "Funcionario não encontrado";
        }
    }

    @PostMapping("/atualizar")
    public String atualizarUsuario(@ModelAttribute UsuarioAtualizarDTO dto,
                                   RedirectAttributes redirect) {

        usuarioService.atualizar(dto.id(), dto);

        redirect.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        return "redirect:/funcionarios";
    }

    @PostMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) throws Exception {
        usuarioService.deletar(id);
        return "redirect:/funcionarios";
    }
}
