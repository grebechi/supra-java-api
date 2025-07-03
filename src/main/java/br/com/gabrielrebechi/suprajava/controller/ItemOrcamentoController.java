package br.com.gabrielrebechi.suprajava.controller;


import br.com.gabrielrebechi.suprajava.dto.ItemOrcamentoCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.ItemOrcamentoDTO;
import br.com.gabrielrebechi.suprajava.dto.ProdutoDTO;
import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaDTO;
import br.com.gabrielrebechi.suprajava.model.*;
import br.com.gabrielrebechi.suprajava.repository.ItemOrcamentoRepository;
import br.com.gabrielrebechi.suprajava.repository.OrcamentoRepository;
import br.com.gabrielrebechi.suprajava.repository.ProdutoRepository;
import br.com.gabrielrebechi.suprajava.service.OrcamentoService;
import br.com.gabrielrebechi.suprajava.util.TempoUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/itens")
public class ItemOrcamentoController {

    private final ItemOrcamentoRepository itemRepo;
    private final ProdutoRepository produtoRepo;
    private final OrcamentoRepository orcamentoRepo;
    private final OrcamentoService orcamentoService;

    public ItemOrcamentoController(ItemOrcamentoRepository itemRepo,
                                   ProdutoRepository produtoRepo,
                                   OrcamentoRepository orcamentoRepo,
                                   OrcamentoService orcamentoService) {
        this.itemRepo = itemRepo;
        this.produtoRepo = produtoRepo;
        this.orcamentoRepo = orcamentoRepo;
        this.orcamentoService = orcamentoService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ItemOrcamentoCreateDTO dto) {
        Produto produto = produtoRepo.findById(dto.produtoId()).orElse(null);
        Orcamento orcamento = orcamentoRepo.findById(dto.orcamentoId()).orElse(null);

        if (produto == null || orcamento == null) {
            return ResponseEntity.badRequest().body("Produto ou orçamento não encontrado.");
        }

        BigDecimal quantidade;
        try {
            quantidade = parseQuantidadeComValidacao(dto.quantidade(), produto.getUnidadeMedida().getTipo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        ItemOrcamento item = new ItemOrcamento(orcamento, produto, quantidade, dto.valorTotal());
        item.atualizarPrecoUnitario();
        ItemOrcamento salvo = itemRepo.save(item);

        orcamentoService.atualizarValorTotal(dto.orcamentoId());
        return ResponseEntity.ok(toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ItemOrcamentoCreateDTO dto) {
        var itemOptional = itemRepo.findById(id);
        var produtoOptional = produtoRepo.findById(dto.produtoId());
        var orcamentoOptional = orcamentoRepo.findById(dto.orcamentoId());

        if (itemOptional.isEmpty() || produtoOptional.isEmpty() || orcamentoOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Dados inválidos.");
        }

        var item = itemOptional.get();
        var produto = produtoOptional.get();
        var orcamento = orcamentoOptional.get();

        BigDecimal quantidade;
        try {
            quantidade = parseQuantidadeComValidacao(dto.quantidade(), produto.getUnidadeMedida().getTipo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        item.setProduto(produto);
        item.setOrcamento(orcamento);
        item.setQuantidade(quantidade);
        item.setValorTotal(dto.valorTotal());
        item.atualizarPrecoUnitario();

        itemRepo.save(item);
        orcamentoService.atualizarValorTotal(dto.orcamentoId());

        return ResponseEntity.ok(toDTO(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return itemRepo.findById(id).map(item -> {
            Long orcamentoId = item.getOrcamento().getId();
            itemRepo.delete(item);
            orcamentoService.atualizarValorTotal(orcamentoId);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ItemOrcamentoDTO> listar() {
        return itemRepo.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrcamentoDTO> buscarPorId(@PathVariable Long id) {
        return itemRepo.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------
    // Métodos auxiliares
    // -------------------

    private BigDecimal parseQuantidadeComValidacao(String entrada, TipoUnidade tipo) {
        if (tipo == TipoUnidade.TEMPO) {
            if (!entrada.contains(":")) {
                throw new IllegalArgumentException("A unidade é de tempo. Use o formato HH:mm ou HH:mm:ss.");
            }
            return TempoUtils.tempoParaDecimal(entrada);
        }

        if (entrada.contains(":")) {
            throw new IllegalArgumentException("A unidade não é de tempo. Use um número decimal (ex: 2.5).");
        }

        BigDecimal valor = new BigDecimal(entrada);

        if (tipo == TipoUnidade.INTEIRA && valor.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException("A unidade exige um valor inteiro. Ex: 1, 2, 3...");
        }

        return valor;
    }

    private ItemOrcamentoDTO toDTO(ItemOrcamento item) {
        Produto p = item.getProduto();

        UnidadeMedida um = p.getUnidadeMedida();
        UnidadeMedidaDTO umDTO = new UnidadeMedidaDTO(
                um.getId(), um.getNome(), um.getSigla(), um.getTipo(), um.getDescricao());

        ProdutoDTO produtoDTO = new ProdutoDTO(
                p.getId(), p.getNome(), umDTO, p.getDescricao()
        );

        String quantidadeStr = um.getTipo() == TipoUnidade.TEMPO
                ? TempoUtils.decimalParaTempo(item.getQuantidade())
                : item.getQuantidade().toPlainString();

        return new ItemOrcamentoDTO(
                item.getId(),
                produtoDTO,
                quantidadeStr,
                item.getValorTotal(),
                item.getPrecoUnitario()
        );
    }
}
