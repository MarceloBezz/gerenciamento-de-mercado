package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.dto.UsuarioDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import br.com.fatec.erp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {
    private final UsuarioService usuarioService;

    public PageController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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

    @GetMapping("/cadastrar")
    public String cadastrarFuncionario(@AuthenticationPrincipal UsuarioSecurity usuario, Model model) {
        model.addAttribute("usuario", usuario);
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO(null, null, null, null));
        }
        return "cadastro";
    }

    @PostMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(@Valid UsuarioDTO usuarioDTO, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "cadastro";
        }

        try {
            usuarioService.cadastrar(usuarioDTO);
            redirect.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/cadastrar";
        } catch (Exception e) {
            result.rejectValue("email", "email", "Email já cadastrado!");
            return "cadastro";
        }
    }
    
}
