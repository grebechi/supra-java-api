package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.ProdutoCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.ProdutoDTO;
import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaDTO;
import br.com.gabrielrebechi.suprajava.model.Produto;
import br.com.gabrielrebechi.suprajava.model.UnidadeMedida;
import br.com.gabrielrebechi.suprajava.repository.ProdutoRepository;
import br.com.gabrielrebechi.suprajava.repository.UnidadeMedidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final UnidadeMedidaRepository unidadeRepository;

    public ProdutoController(ProdutoRepository produtoRepository, UnidadeMedidaRepository unidadeRepository) {
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    @GetMapping
    public List<ProdutoDTO> listar() {
        return produtoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoCreateDTO dto) {
        if (produtoRepository.existsByNomeIgnoreCase(dto.nome())) {
            return ResponseEntity.badRequest().body("Erro: nome do produto já está em uso.");
        }

        UnidadeMedida unidade = unidadeRepository.findById(dto.unidadeMedidaId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada."));

        Produto novo = new Produto(dto.nome(), unidade, dto.descricao());
        Produto salvo = produtoRepository.save(novo);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProdutoCreateDTO dto) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    if (!produto.getNome().equalsIgnoreCase(dto.nome())
                            && produtoRepository.existsByNomeIgnoreCase(dto.nome())) {
                        return ResponseEntity.badRequest().body("Erro: nome do produto já está em uso.");
                    }

                    UnidadeMedida unidade = unidadeRepository.findById(dto.unidadeMedidaId())
                            .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada."));

                    produto.setNome(dto.nome());
                    produto.setUnidadeMedida(unidade);
                    produto.setDescricao(dto.descricao());

                    Produto atualizado = produtoRepository.save(produto);
                    return ResponseEntity.ok(toDTO(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ProdutoDTO toDTO(Produto p) {
        UnidadeMedida u = p.getUnidadeMedida();
        UnidadeMedidaDTO unidadeDTO = new UnidadeMedidaDTO(
                u.getId(), u.getNome(), u.getSigla(), u.getTipo(), u.getDescricao()
        );

        return new ProdutoDTO(
                p.getId(), p.getNome(), unidadeDTO, p.getDescricao()
        );
    }
}
