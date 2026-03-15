package br.com.fatec.erp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

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
}
