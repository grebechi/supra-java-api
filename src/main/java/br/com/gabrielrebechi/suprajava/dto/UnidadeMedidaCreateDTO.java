package br.com.gabrielrebechi.suprajava.dto;

import br.com.gabrielrebechi.suprajava.model.TipoUnidade;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UnidadeMedidaCreateDTO", description = "DTO para criação de unidade de medida")
public record UnidadeMedidaCreateDTO(
        @Schema(description = "Nome da unidade de medida", example = "Quilograma")
        String nome,
        @Schema(description = "Sigla da unidade", example = "kg")
        String sigla,
        @Schema(description = "Tipo da unidade", example = "FRACIONADA")
        TipoUnidade tipo,
        @Schema(description = "Descrição adicional", example = "Medida utilizada para pesagem")
        String descricao
) {}

