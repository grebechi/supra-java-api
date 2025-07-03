package br.com.gabrielrebechi.suprajava.dto;

import java.math.BigDecimal;

public record ItemOrcamentoCreateDTO(
        Long orcamentoId,
        Long produtoId,
        String quantidade, // Pode ser "2", "2:30", "02:30:00"
        BigDecimal valorTotal
) {}