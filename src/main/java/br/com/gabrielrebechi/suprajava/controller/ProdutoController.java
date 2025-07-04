package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.*;
import br.com.gabrielrebechi.suprajava.model.*;
import br.com.gabrielrebechi.suprajava.repository.ItemOrcamentoRepository;
import br.com.gabrielrebechi.suprajava.repository.ProdutoRepository;
import br.com.gabrielrebechi.suprajava.repository.UnidadeMedidaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Produtos", description = "Gerencia os produtos da aplicação")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final UnidadeMedidaRepository unidadeRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public ProdutoController(ProdutoRepository produtoRepository, UnidadeMedidaRepository unidadeRepository, ItemOrcamentoRepository itemOrcamentoRepository) {
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    @GetMapping
    @Operation(summary = "Lista paginada de produtos")
    public Page<ProdutoDTO> listar(Pageable pageable, @RequestParam(required = false) String busca) {
        Page<Produto> produtos;

        if (busca == null || busca.isBlank()) {
            produtos = produtoRepository.findAll(pageable);
        } else {
            produtos = produtoRepository.buscarPorNomeOuDescricao(busca, pageable);
        }

        return produtos.map(this::toDTO);
    }

    @Operation(summary = "Busca um produto pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cadastra um novo produto")
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

    @Operation(summary = "Atualiza um produto pelo ID")
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

    @Operation(summary = "Deleta um produto pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista as melhores ofertas para o produto informado")
    @GetMapping("/{id}/melhores-ofertas")
    public ResponseEntity<?> listarMelhoresOfertas(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto == null) {
            return ResponseEntity.badRequest().body("Produto não encontrado.");
        }

        List<ItemOrcamento> itens = itemOrcamentoRepository.buscarPorProdutoOrdenadoPorPreco(id);

        List<MelhorOfertaDTO> ofertas = itens.stream().map(item -> {
            Orcamento o = item.getOrcamento();
            Fornecedor f = o.getFornecedor();

            FornecedorDTO fornecedorDTO = new FornecedorDTO(
                    f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone()
            );

            return new MelhorOfertaDTO(
                    produto.getNome(),
                    item.getPrecoUnitario(),
                    item.getQuantidade(),
                    item.getValorTotal(),
                    o.getData(),
                    o.getObservacao(),
                    fornecedorDTO
            );
        }).toList();

        return ResponseEntity.ok(ofertas);
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
