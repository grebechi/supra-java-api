package br.com.gabrielrebechi.suprajava.dto;

public record FornecedorCreateDTO(
        String nome,
        String cnpj,
        String email,
        String telefone
) {}
