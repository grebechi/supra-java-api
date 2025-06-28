package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaDTO;
import br.com.gabrielrebechi.suprajava.model.UnidadeMedida;
import br.com.gabrielrebechi.suprajava.repository.UnidadeMedidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeMedidaController {

    private final UnidadeMedidaRepository repository;

    public UnidadeMedidaController(UnidadeMedidaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<UnidadeMedidaDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(unidade -> ResponseEntity.ok(toDTO(unidade)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UnidadeMedidaCreateDTO dto) {
        if (repository.silgaJaRegistrada(dto.sigla())) {
            throw new IllegalArgumentException("Sigla já cadastrada.");
        }

        UnidadeMedida nova = new UnidadeMedida(dto.nome(), dto.sigla(), dto.tipo(), dto.descricao());
        UnidadeMedida salva = repository.save(nova);
        return ResponseEntity.ok(toDTO(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaDTO> atualizar(@PathVariable Long id, @RequestBody UnidadeMedidaCreateDTO dto) {
        return repository.findById(id)
                .map(unidade -> {
                    unidade.setNome(dto.nome());
                    unidade.setSigla(dto.sigla());
                    unidade.setTipo(dto.tipo());
                    unidade.setDescricao(dto.descricao());
                    return ResponseEntity.ok(toDTO(repository.save(unidade)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UnidadeMedidaDTO toDTO(UnidadeMedida u) {
        return new UnidadeMedidaDTO(u.getId(), u.getNome(), u.getSigla(), u.getTipo(), u.getDescricao());
    }
}
