package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.FornecedorDTO;
import br.com.gabrielrebechi.suprajava.dto.OrcamentoCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.OrcamentoDTO;
import br.com.gabrielrebechi.suprajava.model.Fornecedor;
import br.com.gabrielrebechi.suprajava.model.Orcamento;
import br.com.gabrielrebechi.suprajava.repository.FornecedorRepository;
import br.com.gabrielrebechi.suprajava.repository.OrcamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository orcamentoRepository;
    private final FornecedorRepository fornecedorRepository;

    public OrcamentoController(OrcamentoRepository orcamentoRepository, FornecedorRepository fornecedorRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @GetMapping
    public List<OrcamentoDTO> listar() {
        return orcamentoRepository.listarTodosOrdenadosPorData().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoDTO> buscarPorId(@PathVariable Long id) {
        return orcamentoRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody OrcamentoCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId())
                .orElse(null);

        if (fornecedor == null) {
            return ResponseEntity.badRequest().body("Fornecedor não encontrado.");
        }

        Orcamento novo = new Orcamento(fornecedor, dto.data(), dto.nome(), dto.observacao());
        Orcamento salvo = orcamentoRepository.save(novo);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OrcamentoCreateDTO dto) {
        return orcamentoRepository.findById(id)
                .map(orcamento -> {
                    Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId())
                            .orElse(null);

                    if (fornecedor == null) {
                        return ResponseEntity.badRequest().body("Fornecedor não encontrado.");
                    }

                    orcamento.setFornecedor(fornecedor);
                    orcamento.setNome(dto.nome());
                    orcamento.setData(dto.data());
                    orcamento.setObservacao(dto.observacao());

                    Orcamento atualizado = orcamentoRepository.save(orcamento);
                    return ResponseEntity.ok(toDTO(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!orcamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orcamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private OrcamentoDTO toDTO(Orcamento o) {
        Fornecedor f = o.getFornecedor();
        FornecedorDTO fornecedorDTO = new FornecedorDTO(f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone());

        return new OrcamentoDTO(
                o.getId(),
                o.getNome(),
                fornecedorDTO,
                o.getData(),
                o.getValorTotal(),
                o.getObservacao()
        );
    }
}
