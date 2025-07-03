package br.com.gabrielrebechi.suprajava.dto;

import br.com.gabrielrebechi.suprajava.model.TipoUnidade;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UnidadeMedidaDTO", description = "DTO referente à unidade de medida")
public record UnidadeMedidaDTO(
        @Schema(description = "ID da unidade de medida", example = "1")
        Long id,
        @Schema(description = "Nome da unidade de medida", example = "Quilograma")
        String nome,
        @Schema(description = "Sigla da unidade", example = "kg")
        String sigla,
        @Schema(description = "Tipo de unidade", example = "FRACIONADA")
        TipoUnidade tipo,
        @Schema(description = "Descrição adicional", example = "Medida utilizada para pesagem")
        String descricao
) {}
