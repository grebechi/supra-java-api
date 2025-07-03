package br.com.gabrielrebechi.suprajava.controller;

import br.com.gabrielrebechi.suprajava.dto.FornecedorCreateDTO;
import br.com.gabrielrebechi.suprajava.dto.FornecedorDTO;
import br.com.gabrielrebechi.suprajava.model.Fornecedor;
import br.com.gabrielrebechi.suprajava.repository.FornecedorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fornecedores", description = "Gerencia fornecedores de produtos")
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorRepository repository;

    public FornecedorController(FornecedorRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Lista todos os fornecedores")
    @GetMapping
    public List<FornecedorDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Operation(summary = "Busca um fornecedor pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cadastra um novo fornecedor")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody FornecedorCreateDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            return ResponseEntity.badRequest().body("Erro: já existe um fornecedor com este nome.");
        }

        Fornecedor fornecedor = new Fornecedor(
                dto.nome(), dto.cnpj(), dto.email(), dto.telefone()
        );
        Fornecedor salvo = repository.save(fornecedor);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @Operation(summary = "Atualiza um fornecedor pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody FornecedorCreateDTO dto) {
        return repository.findById(id)
                .map(fornecedor -> {
                    if (!fornecedor.getNome().equalsIgnoreCase(dto.nome()) &&
                            repository.existsByNomeIgnoreCase(dto.nome())) {
                        return ResponseEntity.badRequest().body("Erro: já existe um fornecedor com este nome.");
                    }

                    fornecedor.setNome(dto.nome());
                    fornecedor.setCnpj(dto.cnpj());
                    fornecedor.setEmail(dto.email());
                    fornecedor.setTelefone(dto.telefone());

                    Fornecedor atualizado = repository.save(fornecedor);
                    return ResponseEntity.ok(toDTO(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um fornecedor pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private FornecedorDTO toDTO(Fornecedor f) {
        return new FornecedorDTO(f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone());
    }
}
