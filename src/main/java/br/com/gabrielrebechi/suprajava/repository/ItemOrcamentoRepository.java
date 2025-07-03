package br.com.gabrielrebechi.suprajava.repository;

import br.com.gabrielrebechi.suprajava.model.ItemOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {

    @Query("SELECT i FROM ItemOrcamento i WHERE i.orcamento.id = :orcamentoId")
    List<ItemOrcamento> buscarPorOrcamentoId(Long orcamentoId);

}
