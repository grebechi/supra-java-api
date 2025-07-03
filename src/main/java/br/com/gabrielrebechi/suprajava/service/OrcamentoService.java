package br.com.gabrielrebechi.suprajava.service;

import br.com.gabrielrebechi.suprajava.model.ItemOrcamento;
import br.com.gabrielrebechi.suprajava.repository.ItemOrcamentoRepository;
import br.com.gabrielrebechi.suprajava.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository, ItemOrcamentoRepository itemOrcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    public void atualizarValorTotal(Long orcamentoId) {
        BigDecimal total = itemOrcamentoRepository.buscarPorOrcamentoId(orcamentoId).stream()
                .map(ItemOrcamento::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orcamentoRepository.findById(orcamentoId).ifPresent(orcamento -> {
            orcamento.setValorTotal(total);
            orcamentoRepository.save(orcamento);
        });
    }
}
