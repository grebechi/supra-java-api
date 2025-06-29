package br.com.gabrielrebechi.suprajava.repository;

import br.com.gabrielrebechi.suprajava.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    @Query("SELECT COUNT(f) > 0 FROM Fornecedor f WHERE LOWER(f.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(@Param("nome") String nome);

    @Query("SELECT COUNT(f) > 0 FROM Fornecedor f WHERE f.cnpj = :cnpj")
    boolean existsByCnpj(@Param("cnpj") String cnpj);
}
