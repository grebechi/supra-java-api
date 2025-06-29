package br.com.gabrielrebechi.suprajava.view;

import br.com.gabrielrebechi.suprajava.dto.FornecedorDTO;
import br.com.gabrielrebechi.suprajava.dto.ProdutoDTO;
import br.com.gabrielrebechi.suprajava.dto.UnidadeMedidaDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> menuUnidadeMedida();
                case 2 -> menuProduto();
                case 3 -> menuFornecedor();
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
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> listarProdutos(baseUrl);
                case 2 -> cadastrarProduto(baseUrl);
                case 3 -> buscarProdutoPorId(baseUrl);
                case 4 -> atualizarProduto(baseUrl);
                case 5 -> deletarProduto(baseUrl);
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
}
