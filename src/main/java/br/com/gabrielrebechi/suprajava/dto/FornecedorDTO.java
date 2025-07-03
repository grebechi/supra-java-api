package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FornecedorDTO", description = "DTO referente ao fornecedor")
public record FornecedorDTO(
        @Schema(description = "ID do fornecedor", example = "42")
        Long id,
        @Schema(description = "Nome do fornecedor", example = "Construtora XYZ LTDA")
        String nome,
        @Schema(description = "CNPJ do fornecedor", example = "12.345.678/0001-99")
        String cnpj,
        @Schema(description = "Email de contato", example = "contato@xyz.com.br")
        String email,
        @Schema(description = "Telefone de contato", example = "(11) 91234-5678")
        String telefone
) {}
