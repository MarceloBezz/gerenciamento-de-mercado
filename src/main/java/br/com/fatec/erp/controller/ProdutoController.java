package br.com.fatec.erp.controller;

import br.com.fatec.erp.exception.ProdutoNaoEncontradoException;
import br.com.fatec.erp.model.dto.ProdutoDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.fatec.erp.model.Produto;
import br.com.fatec.erp.service.ProdutoService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @PostMapping("/cadastrar")
    public String cadastrarProduto(@Valid @ModelAttribute("produto") ProdutoDTO dto, BindingResult result,
            RedirectAttributes redirect,
            @AuthenticationPrincipal UsuarioSecurity usuario, Model model) throws ProdutoNaoEncontradoException {
        model.addAttribute("usuario", usuario);
        // TODO: Retornar o campo que deu erro no tratador de erros
        if (result.hasErrors()) return "cadastro-produto";

        String mensagem = "";
        if (dto.id() != null) {
            produtoService.alterarProduto(dto);
            mensagem = "Produto alterado com sucesso!";
        } else {
            produtoService.salvarProduto(dto);
            mensagem = "Produto cadastrado com sucesso!";
        }
        redirect.addFlashAttribute("sucesso", mensagem);

        String url = "redirect:/produtos/cadastrar";
        if (dto.id() != null)
            url += "?id=" + dto.id();
        return url;
    }

}
