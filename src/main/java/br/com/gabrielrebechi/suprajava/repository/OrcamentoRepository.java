package br.com.gabrielrebechi.suprajava.repository;

import br.com.gabrielrebechi.suprajava.model.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    @Query("SELECT o FROM Orcamento o WHERE LOWER(o.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(o.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Orcamento> buscarPorNomeOuObservacao(@Param("termo") String termo, Pageable pageable);


    @Query("SELECT o FROM Orcamento o WHERE o.fornecedor.id = :id ORDER BY o.data DESC")
    List<Orcamento> buscarPorFornecedorId(Long id);
}
