package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaDTO;
import br.com.gabrielrebechi.suprajava.model.UnidadeMedida;
import br.com.gabrielrebechi.suprajava.repository.ProdutoRepository;
import br.com.gabrielrebechi.suprajava.repository.UnidadeMedidaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Unidades de Medida", description = "Gerencia unidades de medida dos produtos")
@RestController
@RequestMapping("/api/unidades")
public class UnidadeMedidaController {

    private final UnidadeMedidaRepository repository;
    private final ProdutoRepository produtoRepository;

    public UnidadeMedidaController(UnidadeMedidaRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    @Operation(summary = "Lista paginada de unidades de medida com filtro opcional")
    @GetMapping
    public Page<UnidadeMedidaDTO> listar(Pageable pageable, @RequestParam(required = false) String busca) {
        Page<UnidadeMedida> unidades;

        if (busca == null || busca.isBlank()) {
            unidades = repository.findAll(pageable);
        } else {
            unidades = repository.findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCase(busca, busca, pageable);
        }

        return unidades.map(this::toDTO);
    }


    @Operation(summary = "Busca uma unidade de medida pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(unidade -> ResponseEntity.ok(toDTO(unidade)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cadastra uma nova unidade de medida")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UnidadeMedidaCreateDTO dto) {
        if (repository.silgaJaRegistrada(dto.sigla())) {
            throw new IllegalArgumentException("Sigla já cadastrada.");
        }

        UnidadeMedida nova = new UnidadeMedida(dto.nome(), dto.sigla(), dto.tipo(), dto.descricao());
        UnidadeMedida salva = repository.save(nova);
        return ResponseEntity.ok(toDTO(salva));
    }

    @Operation(summary = "Atualiza uma unidade de medida pelo ID")
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

    @Operation(summary = "Deleta uma unidade de medida pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        boolean possuiProdutosRelacionados = produtoRepository.existsByUnidadeMedidaId(id);
        if (possuiProdutosRelacionados) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build(); // Ou pode retornar uma mensagem mais descritiva se quiser.
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UnidadeMedidaDTO toDTO(UnidadeMedida u) {
        return new UnidadeMedidaDTO(u.getId(), u.getNome(), u.getSigla(), u.getTipo(), u.getDescricao());
    }
}
