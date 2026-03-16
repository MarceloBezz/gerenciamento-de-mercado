package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.Estoque;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.model.dto.LoteDTO;
import br.com.fatec.erp.service.EstoqueService;
import br.com.fatec.erp.service.LoteService;
import br.com.fatec.erp.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final LoteService loteService;
    private final EstoqueService estoqueService;

    public ApiController(LoteService loteService, EstoqueService estoqueService) {
        this.loteService = loteService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/financeiro/entradas-saidas")
    public void listarEntradasESaidas(@RequestParam(required = false) String periodo) {
        // TODO Implementar método
    }

    @GetMapping("/financeiro/vendas/vendedores")
    public void listarVendasPorVendedores() {
        // TODO Implementar método
    }

    @GetMapping("/financeiro/vendas/produtos")
    public void listarProdutosVendidos(@RequestParam(required = false) String periodo) {
        // TODO Implementar método
    }

    @GetMapping("/financeiro/dashboard")
    public List<EstoqueProdutoValidade> listarProdutos() {
        var produtos = estoqueService.listarEstoqueComProdutos();
        return produtos;
    }

    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarLote(@RequestBody @Valid LoteDTO dto) {
        try {
            var lote = loteService.cadastrar(dto);
            return ResponseEntity.ok().body(lote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
