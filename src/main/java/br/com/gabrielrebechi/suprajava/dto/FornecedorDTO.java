package br.com.gabrielrebechi.suprajava.dto;

public record FornecedorDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone
) {}
