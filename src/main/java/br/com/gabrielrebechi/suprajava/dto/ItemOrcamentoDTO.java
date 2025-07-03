package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ItemOrcamentoDTO", description = "DTO referente ao item de orçamento")
public record ItemOrcamentoDTO(
        @Schema(description = "ID do item", example = "101")
        Long id,
        @Schema(description = "Produto incluído no orçamento")
        ProdutoDTO produto,
        @Schema(description = "Quantidade do item", example = "3.5 ou 02:35")
        String quantidade,
        @Schema(description = "Valor total do item", example = "98.50")
        BigDecimal valorTotal,
        @Schema(description = "Preço unitário calculado", example = "28.14")
        BigDecimal precoUnitario
) {}
