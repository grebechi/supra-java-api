package br.com.gabrielrebechi.suprajava.dto;

import java.math.BigDecimal;

public record ItemOrcamentoDTO(
        Long id,
        ProdutoDTO produto,
        String quantidade,
        BigDecimal valorTotal,
        BigDecimal precoUnitario
) {}
