package br.com.fatec.erp.controller;

import br.com.fatec.erp.security.UsuarioSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @GetMapping({"/login", "/"})
    public String login(@RequestParam(required = false) String logout,
                        @RequestParam(required = false) String error, Model model) {
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
    public String telaCadastrarFuncionario() {
//        model.addAttribute("usuario", usuario);
//        if (!model.containsAttribute("usuarioDTO")) {
//            model.addAttribute("usuarioDTO", new UsuarioDTO(null, null, null, null));
//        }
        return "cadastro-funcionario";
    }

    @GetMapping("/produtos/cadastrar")
    public String telaCadastrarProduto() {
        return "cadastro-produto";
    }
}
