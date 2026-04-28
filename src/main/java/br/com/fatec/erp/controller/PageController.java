package br.com.fatec.erp.controller;

import br.com.fatec.erp.exception.ProdutoNaoEncontradoException;
import br.com.fatec.erp.model.Usuario;
import br.com.fatec.erp.model.dto.ProdutoDTO;
import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import br.com.fatec.erp.service.ProdutoService;
import br.com.fatec.erp.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;

    public PageController(ProdutoService produtoService, UsuarioService usuarioService) {
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/login", "/"})
    public String login(@RequestParam(required = false) String logout,
                        @RequestParam(required = false) String error, Model model,
                        Authentication authentication) {
        if (authentication != null)
            return "redirect:/home";

        if (logout != null)
            model.addAttribute("logout", logout);
        if (error != null)
            model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {
        model.addAttribute("usuario", usuario.getUsuario());
        return "home";
    }

    @GetMapping("/funcionarios/cadastrar")
    public String telaCadastrarFuncionario(Model model, @AuthenticationPrincipal UsuarioSecurity usuarioSecurity) {
       model.addAttribute("usuario", usuarioSecurity.getUsuario());
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO(null, null, null, null, null));
        }
        return "cadastro-funcionario";
    }

    @GetMapping("/produtos/cadastrar")
    public String telaCadastrarProduto(@AuthenticationPrincipal UsuarioSecurity usuario, Model model,
                                       @RequestParam(required = false) Long id) throws ProdutoNaoEncontradoException {
        if (id != null) {
            ProdutoDTO produto = produtoService.buscarPorId(id);
            model.addAttribute("produto", produto);
        } else {
            model.addAttribute("produto", new ProdutoDTO(null, null, null, null, null));
        } 
        model.addAttribute("usuario", usuario.getUsuario());

        return "cadastro-produto";
    }

    @GetMapping("/estoque")
    public String estoque(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {
        model.addAttribute("usuario", usuario.getUsuario());
        return "estoque";
    }

    @GetMapping("/vendas")
    public String vendas(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {
        model.addAttribute("usuario", usuario.getUsuario());
        return "vendas";
    }

    @GetMapping("/meus-dados")
    public String meusDados(@AuthenticationPrincipal UsuarioSecurity usuarioSecurity, Model model) {
        Usuario usuario = usuarioSecurity.getUsuario();
        UsuarioDTO dto = new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), null, usuario.getCargo());
        model.addAttribute("usuario", usuarioSecurity.getUsuario());
        model.addAttribute("usuarioDTO", dto);
        return "meus-dados";
    }


    @GetMapping("/error")
    public String erro(@AuthenticationPrincipal UsuarioSecurity usuarioSecurity) {
        return "error";
    }

}
