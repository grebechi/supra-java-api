package br.com.gabrielrebechi.suprajava.dto;

public record ProdutoDTO(
        Long id,
        String nome,
        UnidadeMedidaDTO unidadeMedida,
        String descricao
) {}
