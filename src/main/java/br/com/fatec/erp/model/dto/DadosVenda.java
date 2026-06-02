package br.com.fatec.erp.model.dto;

import br.com.fatec.erp.model.venda.Venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DadosVenda(
        Long idVenda,
        String idVendedor,
        BigDecimal total,
        LocalDate dataVenda,
        List<DadosVendaProduto> itens
) {
    public DadosVenda(Venda venda) {
        this(
                venda.getId(),
                venda.getVendedor().getNome(),
                venda.getTotal(),
                venda.getDataVenda(),
                DadosVendaProduto.retornaLista(venda.getItens())
        );
    }
}
