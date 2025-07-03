package br.com.gabrielrebechi.suprajava.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoCreateDTO", description = "DTO para criação de produto")
public record ProdutoCreateDTO(
        @Schema(description = "Nome do produto", example = "Cimento CP II 50kg")
        String nome,
        @Schema(description = "ID da unidade de medida", example = "1")
        Long unidadeMedidaId,
        @Schema(description = "Descrição do produto", example = "Saco de cimento para obras")
        String descricao
) {}
