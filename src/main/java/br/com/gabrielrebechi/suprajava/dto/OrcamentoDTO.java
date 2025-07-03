package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "OrcamentoDTO", description = "DTO referente ao orçamento")
public record OrcamentoDTO(
        @Schema(description = "ID do orçamento", example = "202")
        Long id,
        @Schema(description = "Nome do orçamento", example = "Orçamento Janeiro")
        String nome,
        @Schema(description = "Fornecedor relacionado")
        FornecedorDTO fornecedor,
        @Schema(description = "Data do orçamento", example = "2025-01-15")
        LocalDate data,
        @Schema(description = "Valor total do orçamento", example = "350.00")
        BigDecimal valorTotal,
        @Schema(description = "Observações gerais", example = "Entrega em até 10 dias úteis")
        String observacao,
        @Schema(description = "Itens incluídos")
        List<ItemOrcamentoDTO> itens
) {}
