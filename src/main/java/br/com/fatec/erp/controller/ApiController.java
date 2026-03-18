package br.com.fatec.erp.controller;

import br.com.fatec.erp.model.dto.DashboardEstoqueResumo;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.model.dto.LoteDTO;
import br.com.fatec.erp.model.dto.VendaProdutoDTO;
import br.com.fatec.erp.service.EstoqueService;
import br.com.fatec.erp.service.LoteService;
import br.com.fatec.erp.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final LoteService loteService;
    private final EstoqueService estoqueService;
    private final VendaService vendaService;

    public ApiController(LoteService loteService, EstoqueService estoqueService, VendaService vendaService) {
        this.loteService = loteService;
        this.estoqueService = estoqueService;
        this.vendaService = vendaService;
    }

    @GetMapping("/financeiro/dashboard")
    public Page<EstoqueProdutoValidade> listarProdutos(Pageable pageable, @RequestParam(required = false) String produto,
                                                       @RequestParam(required = false) String status) {
        var produtos = estoqueService.listarEstoqueComProdutos(pageable, produto, status);
        return produtos;
    }

    @GetMapping("/financeiro/dashboard/resumo")
    public DashboardEstoqueResumo resumo() {
        return estoqueService.buscarResumo();
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

    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarLote(@RequestBody @Valid LoteDTO dto) {
        try {
            var lote = loteService.cadastrar(dto);
            return ResponseEntity.ok().body(lote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/vendas")
    public ResponseEntity<?> cadastrarVenda(@RequestBody List<VendaProdutoDTO> dtos) {
        try {
            var venda = vendaService.cadastrar(dtos);
            return ResponseEntity.ok().body(venda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
