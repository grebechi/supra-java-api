package br.com.gabrielrebechi.suprajava.dto;

import br.com.gabrielrebechi.suprajava.model.TipoUnidade;

public record UnidadeMedidaDTO(
        Long id,
        String nome,
        String sigla,
        TipoUnidade tipo,
        String descricao
) {}
