package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.venda.Venda;

import java.math.BigDecimal;
import java.util.List;

public record DadosVenda(
        Long idVenda,
        Long idVendedor,
        BigDecimal total,
        List<DadosVendaProduto> itens
) {
    public DadosVenda(Venda venda) {
        this(
                venda.getId(),
                venda.getVendedor().getId(),
                venda.getTotal(),
                DadosVendaProduto.retornaLista(venda.getItens())
        );
    }
}
