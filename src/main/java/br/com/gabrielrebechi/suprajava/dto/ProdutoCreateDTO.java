package br.com.gabrielrebechi.suprajava.dto;

public record ProdutoCreateDTO(
        String nome,
        Long unidadeMedidaId,
        String descricao
) {}
