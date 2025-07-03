package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "OrcamentoCreateDTO", description = "DTO para criação de orçamento")
public record OrcamentoCreateDTO(
        @Schema(description = "Nome do orçamento", example = "Orçamento Janeiro")
        String nome,
        @Schema(description = "ID do fornecedor", example = "42")
        Long fornecedorId,
        @Schema(description = "Data do orçamento", example = "2025-01-15")
        LocalDate data,
        @Schema(description = "Observações gerais", example = "Entrega em até 10 dias úteis")
        String observacao
) {}

