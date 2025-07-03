package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ItemOrcamentoCreateDTO", description = "DTO para criação de item de orçamento")
public record ItemOrcamentoCreateDTO(
        @Schema(description = "ID do orçamento ao qual o item pertence", example = "202")
        Long orcamentoId,
        @Schema(description = "ID do produto", example = "1")
        Long produtoId,
        @Schema(description = "Quantidade do item", example = "3.5")
        String quantidade, // Pode ser "2", "2:30", "02:30:00"
        @Schema(description = "Valor total do item", example = "98.50")
        BigDecimal valorTotal
) {}