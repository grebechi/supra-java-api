package br.com.gabrielrebechi.suprajava.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MelhorOfertaDTO(
        String produto,
        BigDecimal precoUnitario,
        BigDecimal quantidade,
        BigDecimal valorTotal,
        LocalDate dataOrcamento,
        String observacao,
        FornecedorDTO fornecedor
) {}
