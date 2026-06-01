package br.com.fatec.erp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fatec.erp.model.dto.DadosVenda;
import br.com.fatec.erp.model.dto.DashboardEstoqueResumo;
import br.com.fatec.erp.model.dto.EstoqueProdutoValidade;
import br.com.fatec.erp.model.dto.LoteDTO;
import br.com.fatec.erp.model.dto.VendaProdutoDTO;
import br.com.fatec.erp.security.UsuarioSecurity;
import br.com.fatec.erp.service.EstoqueService;
import br.com.fatec.erp.service.LoteService;
import br.com.fatec.erp.service.VendaService;
import jakarta.validation.Valid;

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

    // ================= DASHBOARD FINANCEIRO =================
    @GetMapping("/financeiro/dashboard")
    public Page<EstoqueProdutoValidade> listarProdutos(Pageable pageable, @RequestParam(required = false) String produto,
                                                       @RequestParam(required = false) String status) {
        return estoqueService.listarEstoqueComProdutos(pageable, produto, status);
    }

    @GetMapping("/financeiro/dashboard/resumo")
    public DashboardEstoqueResumo resumo() {
        return estoqueService.buscarResumo();
    }

    // ================= ENTRADAS E SAIDAS =================
    @GetMapping("/financeiro/entradas-saidas")
    public void listarEntradasESaidas(@RequestParam(required = false) String periodo) {
        // TODO Implementar método
    }

    // ================= GESTÃO DE LOTES =================
    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarLote(@RequestBody @Valid LoteDTO dto) {
        try {
            var lote = loteService.cadastrar(dto);
            return ResponseEntity.ok().body(lote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ================= GESTÃO DE VENDAS =================
    @PostMapping("/vendas")
    public ResponseEntity<?> cadastrarVenda(@AuthenticationPrincipal UsuarioSecurity usuarioLogado, @RequestBody List<VendaProdutoDTO> dtos) {

        try {
            var venda = vendaService.cadastrar(usuarioLogado.getUsuario(), dtos);
            return ResponseEntity.ok().body(venda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vendas")
    public Page<DadosVenda> listarVendas(Pageable pageable ){
        var vendas = vendaService.listarVendas(pageable);
        return vendas;
    }
}