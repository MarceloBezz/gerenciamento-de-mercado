package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.dto.ProdutoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
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
    public String cadastrarProduto(@Valid ProdutoDTO dto, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "cadastro-produto";
        }

        try  {
            produtoService.salvarProduto(dto);
            redirect.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
            return "redirect:/produtos/cadastrar";
        } catch (Exception e) {
            result.rejectValue("erro", "Erro ao cadastrar produto!");
            return "cadastro-produto";
        }
    }

}
    
