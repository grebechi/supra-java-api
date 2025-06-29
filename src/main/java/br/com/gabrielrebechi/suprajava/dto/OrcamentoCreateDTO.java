package br.com.gabrielrebechi.suprajava.dto;

import java.time.LocalDate;

public record OrcamentoCreateDTO(
        String nome,
        Long fornecedorId,
        LocalDate data,
        String observacao
) {}

