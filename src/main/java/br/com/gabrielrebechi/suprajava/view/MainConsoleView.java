package br.com.gabrielrebechi.suprajava.view;

import br.com.gabrielrebechi.suprajava.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

@Component
public class MainConsoleView implements CommandLineRunner {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.findAndRegisterModules(); // habilita suporte ao LocalDate, etc.
    }

    @Override
    public void run(String... args) throws Exception {
        exibirMenuPrincipal();
    }

    public void exibirMenuPrincipal() throws Exception {
        while (true) {
            System.out.println("\n=== SUPRAJAVA - MENU PRINCIPAL ===");
            System.out.println("1. Unidade de Medida");
            System.out.println("2. Produto");
            System.out.println("3. Fornecedor");
            System.out.println("4. Orçamentos");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> menuUnidadeMedida();
                case 2 -> menuProduto();
                case 3 -> menuFornecedor();
                case 4 -> menuOrcamento();
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void menuUnidadeMedida() throws Exception {
        String baseUrl = "http://localhost:8080/api/unidades";
        while (true) {
            System.out.println("\n--- MENU: Unidade de Medida ---");
            System.out.println("1. Listar");
            System.out.println("2. Cadastrar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> listarUnidades(baseUrl);
                case 2 -> cadastrarUnidade(baseUrl);
                case 3 -> buscarUnidadePorId(baseUrl);
                case 4 -> atualizarUnidade(baseUrl);
                case 5 -> deletarUnidade(baseUrl);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void menuProduto() throws Exception {
        String baseUrl = "http://localhost:8080/api/produtos";
        while (true) {
            System.out.println("\n--- MENU: Produto ---");
            System.out.println("1. Listar");
            System.out.println("2. Cadastrar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("6. Valores por Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> listarProdutos(baseUrl);
                case 2 -> cadastrarProduto(baseUrl);
                case 3 -> buscarProdutoPorId(baseUrl);
                case 4 -> atualizarProduto(baseUrl);
                case 5 -> deletarProduto(baseUrl);
                case 6 -> buscarMelhoresOfertasPorProduto();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void listarUnidades(String baseUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<UnidadeMedidaDTO> lista = mapper.readValue(response.body(), new TypeReference<>() {});
        lista.forEach(u -> System.out.printf("[%d] %s (%s) - %s\n", u.id(), u.nome(), u.sigla(), u.descricao()));
    }

    private void cadastrarUnidade(String baseUrl) throws Exception {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sigla: ");
        String sigla = scanner.nextLine();
        System.out.print("Tipo (INTEIRA, FRACIONADA, TEMPO): ");
        String tipo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String json = String.format("""
        {
          \"nome\": \"%s\",
          \"sigla\": \"%s\",
          \"tipo\": \"%s\",
          \"descricao\": \"%s\"
        }
        """, nome, sigla, tipo, descricao);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void buscarUnidadePorId(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void atualizarUnidade(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sigla: ");
        String sigla = scanner.nextLine();
        System.out.print("Tipo (INTEIRA, FRACIONADA, TEMPO): ");
        String tipo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String json = String.format("""
        {
          \"nome\": \"%s\",
          \"sigla\": \"%s\",
          \"tipo\": \"%s\",
          \"descricao\": \"%s\"
        }
        """, nome, sigla, tipo, descricao);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void deletarUnidade(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 204 ? "Removido com sucesso" : response.body());
    }

    private void listarProdutos(String baseUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<ProdutoDTO> lista = mapper.readValue(response.body(), new TypeReference<>() {});
        lista.forEach(p -> System.out.printf("[%d] %s (%s) - %s\n",
                p.id(), p.nome(), p.unidadeMedida().sigla(), p.descricao()));
    }

    private void cadastrarProduto(String baseUrl) throws Exception {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("ID da unidade de medida: ");
        Long unidadeId = Long.parseLong(scanner.nextLine());
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String json = String.format("""
        {
          \"nome\": \"%s\",
          \"unidadeMedidaId\": %d,
          \"descricao\": \"%s\"
        }
        """, nome, unidadeId, descricao);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void buscarProdutoPorId(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void atualizarProduto(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("ID da unidade de medida: ");
        Long unidadeId = Long.parseLong(scanner.nextLine());
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String json = String.format("""
        {
          \"nome\": \"%s\",
          \"unidadeMedidaId\": %d,
          \"descricao\": \"%s\"
        }
        """, nome, unidadeId, descricao);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void deletarProduto(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 204 ? "Removido com sucesso" : response.body());
    }

    private void buscarMelhoresOfertasPorProduto() throws Exception {
        System.out.print("ID do produto: ");
        Long produtoId = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/produtos/" + produtoId + "/melhores-ofertas"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<MelhorOfertaDTO> ofertas = mapper.readValue(response.body(), new TypeReference<>() {});
            if (ofertas.isEmpty()) {
                System.out.println("Nenhuma oferta encontrada para o produto informado.");
            } else {
                System.out.println("--- Melhores Ofertas ---");
                for (MelhorOfertaDTO oferta : ofertas) {
                    System.out.printf("\nFornecedor: %s | Preço Unitário: R$ %.2f | Data: %s | Observação: %s \n",
                            oferta.fornecedor().nome(), oferta.precoUnitario(), oferta.dataOrcamento(), oferta.observacao());

                }
            }
        } else {
            System.out.println("Erro ao buscar ofertas: " + response.body());
        }
    }

    private void menuFornecedor() throws Exception {
        String baseUrl = "http://localhost:8080/api/fornecedores";
        while (true) {
            System.out.println("\n--- MENU: Fornecedor ---");
            System.out.println("1. Listar");
            System.out.println("2. Cadastrar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> listarFornecedores(baseUrl);
                case 2 -> cadastrarFornecedor(baseUrl);
                case 3 -> buscarFornecedorPorId(baseUrl);
                case 4 -> atualizarFornecedor(baseUrl);
                case 5 -> deletarFornecedor(baseUrl);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void listarFornecedores(String baseUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<FornecedorDTO> lista = mapper.readValue(response.body(), new TypeReference<>() {});
        lista.forEach(f -> System.out.printf("[%d] %s - CNPJ: %s, Email: %s, Telefone: %s%n",
                f.id(), f.nome(), f.cnpj(), f.email(), f.telefone()));
    }

    private void cadastrarFornecedor(String baseUrl) throws Exception {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        String json = String.format("""
    {
      "nome": "%s",
      "cnpj": "%s",
      "email": "%s",
      "telefone": "%s"
    }
    """, nome, cnpj, email, telefone);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void buscarFornecedorPorId(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void atualizarFornecedor(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        String json = String.format("""
    {
      "nome": "%s",
      "cnpj": "%s",
      "email": "%s",
      "telefone": "%s"
    }
    """, nome, cnpj, email, telefone);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void deletarFornecedor(String baseUrl) throws Exception {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 204 ? "Removido com sucesso" : response.body());
    }

    // Adicionando o menu de Orçamento com as rotas novas
    private void menuOrcamento() throws Exception {
        String baseUrl = "http://localhost:8080/api/orcamentos";
        while (true) {
            System.out.println("\n--- MENU: Orçamento ---");
            System.out.println("1. Listar");
            System.out.println("2. Cadastrar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("6. Adicionar item");
            System.out.println("7. Listar itens");
            System.out.println("8. Atualizar item");
            System.out.println("9. Remover item");
            System.out.println("10. Ver orçamentos por fornecedor");
            System.out.print("0. Voltar\nEscolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> listarOrcamentos(baseUrl);
                case 2 -> cadastrarOrcamento(baseUrl);
                case 3 -> buscarOrcamentoPorId(baseUrl);
                case 4 -> atualizarOrcamento(baseUrl);
                case 5 -> deletarOrcamento(baseUrl);
                case 6 -> adicionarItemOrcamento();
                case 7 -> listarItensOrcamento();
                case 8 -> atualizarItemOrcamento();
                case 9 -> removerItemOrcamento();
                case 10 -> listarOrcamentosPorFornecedor();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void adicionarItemOrcamento() throws Exception {
        System.out.print("ID do orçamento: ");
        Long orcamentoId = Long.parseLong(scanner.nextLine());

        System.out.print("ID do produto: ");
        Long produtoId = Long.parseLong(scanner.nextLine());

        // Buscar o produto para descobrir o tipo da unidade
        HttpRequest produtoRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/produtos/" + produtoId))
                .GET()
                .build();

        HttpResponse<String> produtoResponse = client.send(produtoRequest, HttpResponse.BodyHandlers.ofString());
        ProdutoDTO produto = mapper.readValue(produtoResponse.body(), ProdutoDTO.class);
        String tipo = produto.unidadeMedida().tipo().name();

        String mensagem = switch (tipo) {
            case "TEMPO" -> "Informe a quantidade em formato de tempo (HH:mm): ";
            case "INTEIRA" -> "Informe a quantidade como número inteiro: ";
            case "FRACIONADA" -> "Informe a quantidade com casas decimais: ";
            default -> "Informe a quantidade: ";
        };

        System.out.print(mensagem);
        String quantidade = scanner.nextLine();

        System.out.print("Valor total: ");
        BigDecimal valorTotal = new BigDecimal(scanner.nextLine());

        String json = String.format("""
        {
          "orcamentoId": %d,
          "produtoId": %d,
          "quantidade": "%s",
          "valorTotal": %s
        }
        """, orcamentoId, produtoId, quantidade, valorTotal);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/itens"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void listarItensOrcamento() throws Exception {
        System.out.print("ID do orçamento: ");
        Long id = Long.parseLong(scanner.nextLine());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/orcamentos/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void atualizarItemOrcamento() throws Exception {
        System.out.print("ID do item: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("ID do orçamento: ");
        Long orcamentoId = Long.parseLong(scanner.nextLine());
        System.out.print("ID do produto: ");
        Long produtoId = Long.parseLong(scanner.nextLine());
        System.out.print("Quantidade (número ou HH:mm): ");
        String quantidade = scanner.nextLine();
        System.out.print("Valor total: ");
        BigDecimal valorTotal = new BigDecimal(scanner.nextLine());

        String json = String.format("""
        {
          "orcamentoId": %d,
          "produtoId": %d,
          "quantidade": "%s",
          "valorTotal": %s
        }
        """, orcamentoId, produtoId, quantidade, valorTotal);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/itens/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void removerItemOrcamento() throws Exception {
        System.out.print("ID do item: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/itens/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 204 ? "Item removido com sucesso." : response.body());
    }

    private void listarOrcamentosPorFornecedor() throws Exception {
        System.out.print("ID do fornecedor: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/orcamentos/fornecedor/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void listarOrcamentos(String baseUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<OrcamentoDTO> lista = mapper.readValue(response.body(), new TypeReference<>() {});
        lista.forEach(o -> System.out.printf("[%d] %s | %s - %s (R$ %s)\n",
                o.id(), o.nome(), o.fornecedor().nome(), o.data(), o.valorTotal() != null ? o.valorTotal() : "0.00"));
    }

    private void cadastrarOrcamento(String baseUrl) throws Exception {
        System.out.print("Nome do orçamento: ");
        String nome = scanner.nextLine();

        System.out.print("ID do fornecedor: ");
        Long fornecedorId = Long.parseLong(scanner.nextLine());

        System.out.print("Data (yyyy-MM-dd): ");
        String data = scanner.nextLine();

        System.out.print("Observação: ");
        String obs = scanner.nextLine();

        String json = String.format("""
    {
      "nome": "%s",
      "fornecedorId": %d,
      "data": "%s",
      "observacao": "%s"
    }
    """, nome, fornecedorId, data, obs);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void buscarOrcamentoPorId(String baseUrl) throws Exception {
        System.out.print("ID do orçamento: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void atualizarOrcamento(String baseUrl) throws Exception {
        System.out.print("ID do orçamento: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Novo nome do orçamento: ");
        String nome = scanner.nextLine();

        System.out.print("ID do fornecedor: ");
        Long fornecedorId = Long.parseLong(scanner.nextLine());

        System.out.print("Nova data (yyyy-MM-dd): ");
        String data = scanner.nextLine();

        System.out.print("Nova observação: ");
        String obs = scanner.nextLine();

        String json = String.format("""
    {
      "nome": "%s",
      "fornecedorId": %d,
      "data": "%s",
      "observacao": "%s"
    }
    """, nome, fornecedorId, data, obs);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void deletarOrcamento(String baseUrl) throws Exception {
        System.out.print("ID do orçamento: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 204 ? "Removido com sucesso." : response.body());
    }
}
