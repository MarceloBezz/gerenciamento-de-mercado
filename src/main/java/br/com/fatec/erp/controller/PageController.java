package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.dto.ProdutoDTO;
import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import br.com.fatec.erp.service.ProdutoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    private final ProdutoService produtoService;

    PageController(ProdutoService produtoService) {
        this.produtoService = produtoService;
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
        model.addAttribute("usuario", usuario);
        return "home";
    }

    @GetMapping("/funcionarios/cadastrar")
    public String telaCadastrarFuncionario(Model model) {
//        model.addAttribute("usuario", usuario);
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO(null, null, null, null));
        }
        return "cadastro-funcionario";
    }

    @GetMapping("/produtos/cadastrar")
    public String telaCadastrarProduto(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {

        model.addAttribute("produto", new ProdutoDTO(null, null, null, null));
        model.addAttribute("usuario", usuario);

        return "cadastro-produto";
    }

    @GetMapping("/estoque")
    public String estoque(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {
        model.addAttribute("usuario", usuario);
//        model.addAttribute("produtos", produtoService.listarProdutos());
        return "estoque";
    }
}
