package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoDTO", description = "DTO referente ao produto")
public record ProdutoDTO(
        @Schema(description = "ID do produto", example = "1")
        Long id,
        @Schema(description = "Nome do produto", example = "Cimento CP II 50kg")
        String nome,
        @Schema(description = "Unidade de medida do produto")
        UnidadeMedidaDTO unidadeMedida,
        @Schema(description = "Descrição do produto", example = "Saco de cimento")
        String descricao
) {}
