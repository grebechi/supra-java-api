package br.com.gabrielrebechi.suprajava.repository;

import br.com.gabrielrebechi.suprajava.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Produto p WHERE LOWER(p.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(@Param("nome") String nome);

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :busca, '%'))")
    Page<Produto> buscarPorNomeOuDescricao(@Param("busca") String busca, Pageable pageable);

}
