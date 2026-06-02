package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.venda.VendaProduto;

import java.math.BigDecimal;
import java.util.List;

public record DadosVendaProduto(
        Long produtoId,
        Integer quantidade,
        String nomeProduto,
        BigDecimal valor
) {
    static List<DadosVendaProduto> retornaLista(List<VendaProduto> itens) {
        return itens
                .stream()
                .map(item -> new DadosVendaProduto(item.getId().getProdutoId(), item.getQuantidade(), item.getProduto().getNome(), item.getValor()))
                .toList();
    }
}
