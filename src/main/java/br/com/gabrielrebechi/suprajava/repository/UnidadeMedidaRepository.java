package br.com.gabrielrebechi.suprajava.repository;

import br.com.gabrielrebechi.suprajava.model.UnidadeMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long> {

    @Query("SELECT COUNT(u) > 0 FROM UnidadeMedida u WHERE LOWER(u.sigla) = LOWER(:sigla)")
    boolean silgaJaRegistrada(@Param("sigla") String sigla);

    Page<UnidadeMedida> findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCase(String nome, String sigla, Pageable pageable);

}
