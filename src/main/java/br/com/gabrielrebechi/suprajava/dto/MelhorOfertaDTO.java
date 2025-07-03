package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "MelhorOfertaDTO", description = "DTO que representa a melhor oferta de um fornecedor para um produto")
public record MelhorOfertaDTO(

        @Schema(description = "Nome do produto", example = "Cimento CP II 50kg")
        String produto,

        @Schema(description = "Preço unitário da oferta", example = "27.99")
        BigDecimal precoUnitario,

        @Schema(description = "Quantidade ofertada", example = "3.5")
        BigDecimal quantidade,

        @Schema(description = "Valor total da oferta", example = "97.97")
        BigDecimal valorTotal,

        @Schema(description = "Data do orçamento", example = "2025-01-15")
        LocalDate dataOrcamento,

        @Schema(description = "Observações da oferta", example = "Entrega rápida em até 2 dias úteis")
        String observacao,

        @Schema(description = "Fornecedor responsável pela oferta")
        FornecedorDTO fornecedor

) {}
