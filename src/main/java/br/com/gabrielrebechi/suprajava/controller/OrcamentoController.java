package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.*;
import br.com.gabrielrebechi.suprajava.model.*;
import br.com.gabrielrebechi.suprajava.repository.*;
import br.com.gabrielrebechi.suprajava.util.TempoUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Orçamentos", description = "Gerencia orçamentos com fornecedores e produtos")
@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository orcamentoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public OrcamentoController(OrcamentoRepository orcamentoRepository,
                               FornecedorRepository fornecedorRepository,
                               ItemOrcamentoRepository itemOrcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    @GetMapping
    @Operation(summary = "Lista paginada de orçamentos")
    public Page<OrcamentoDTO> listar(Pageable pageable, @RequestParam(required = false) String busca) {
        Page<Orcamento> orcamentos = (busca == null || busca.isBlank())
                ? orcamentoRepository.findAll(pageable)
                : orcamentoRepository.buscarPorNomeOuObservacao(busca, pageable);

        return orcamentos.map(this::toDTO);
    }


    @Operation(summary = "Busca um orçamento pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoDTO> buscarPorId(@PathVariable Long id) {
        return orcamentoRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cadastra um novo orçamento")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody OrcamentoCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElse(null);

        if (fornecedor == null) {
            return ResponseEntity.badRequest().body("Fornecedor não encontrado.");
        }

        Orcamento novo = new Orcamento(fornecedor, dto.data(), dto.nome(), dto.observacao());
        Orcamento salvo = orcamentoRepository.save(novo);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @Operation(summary = "Atualiza um orçamento pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OrcamentoCreateDTO dto) {
        return orcamentoRepository.findById(id)
                .map(orcamento -> {
                    Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElse(null);
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

    @Operation(summary = "Deleta um orçamento pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!orcamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orcamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista orçamentos de um fornecedor")
    @GetMapping("/fornecedor/{id}")
    public ResponseEntity<?> listarPorFornecedor(@PathVariable Long id) {
        if (!fornecedorRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Fornecedor não encontrado.");
        }

        List<OrcamentoDTO> orcamentos = orcamentoRepository.buscarPorFornecedorId(id).stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(orcamentos);
    }

    private OrcamentoDTO toDTO(Orcamento o) {
        Fornecedor f = o.getFornecedor();
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone()
        );

        List<ItemOrcamentoDTO> itens = itemOrcamentoRepository.buscarPorOrcamentoId(o.getId()).stream()
                .map(this::mapItem)
                .toList();

        return new OrcamentoDTO(
                o.getId(),
                o.getNome(),
                fornecedorDTO,
                o.getData(),
                o.getValorTotal(),
                o.getObservacao(),
                itens
        );
    }

    private ItemOrcamentoDTO mapItem(ItemOrcamento item) {
        Produto p = item.getProduto();

        UnidadeMedida um = p.getUnidadeMedida();
        UnidadeMedidaDTO umDTO = new UnidadeMedidaDTO(
                um.getId(), um.getNome(), um.getSigla(), um.getTipo(), um.getDescricao()
        );

        ProdutoDTO produtoDTO = new ProdutoDTO(
                p.getId(), p.getNome(), umDTO, p.getDescricao()
        );

        String quantidadeFormatada = um.getTipo() == TipoUnidade.TEMPO
                ? TempoUtils.decimalParaTempo(item.getQuantidade())
                : item.getQuantidade().toPlainString();

        return new ItemOrcamentoDTO(
                item.getId(),
                produtoDTO,
                quantidadeFormatada,
                item.getValorTotal(),
                item.getPrecoUnitario()
        );
    }
}
