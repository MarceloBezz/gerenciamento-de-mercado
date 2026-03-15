package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.dto.LoteDTO;
import br.com.fatec.erp.service.LoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final LoteService loteService;

    public ApiController(LoteService loteService) {
        this.loteService = loteService;
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

    @GetMapping("/produtos")
    public void listarProdutos(@RequestParam(required = false) String condicao) {
        // TODO Implementar método
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
