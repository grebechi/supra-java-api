package br.com.gabrielrebechi.suprajava.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoDTO(
        Long id,
        String nome,
        FornecedorDTO fornecedor,
        LocalDate data,
        BigDecimal valorTotal,
        String observacao,
        List<ItemOrcamentoDTO> itens
) {}
