# SupraJava - Sistema de Compras

Este projeto foi desenvolvido como trabalho prático da disciplina de **Programação Orientada a Objetos 1 (POO1)**. O objetivo é aplicar os conceitos de orientação a objetos e desenvolvimento em camadas utilizando **Java**, **Spring Boot**, **JPA** e **PostgreSQL**, simulando o funcionamento de um sistema de compras corporativas.

---

## 🚀 Funcionalidades

O sistema foi planejado para cobrir os seguintes processos de forma simples e funcional:

### Cadastro e gerenciamento de:

* **Unidades de medida** (inteira, fracionada e tempo)
* **Produtos** com descrição e unidade associada
* **Fornecedores** com dados de contato e CNPJ
* **Orçamentos** vinculados a fornecedores
* **Itens de orçamento**, com quantidade, valor total e preço unitário calculado automaticamente

### Recursos adicionais:

* Validação automática de formato da quantidade conforme o tipo de unidade (ex: tempo = HH\:mm)
* Cálculo automático do valor total do orçamento a partir dos itens
* Busca de **melhores ofertas por produto** com base em orçamentos cadastrados
* Interface console (CLI) para testes e interação com a API

---

## 📝 Rotas da API

### Unidade de Medida

* `GET /api/unidades` - Lista todas as unidades
* `GET /api/unidades/{id}` - Retorna unidade por ID
* `POST /api/unidades` - Cria nova unidade
* `PUT /api/unidades/{id}` - Atualiza unidade
* `DELETE /api/unidades/{id}` - Remove unidade

### Produto

* `GET /api/produtos` - Lista todos os produtos
* `GET /api/produtos/{id}` - Retorna produto por ID
* `POST /api/produtos` - Cria novo produto
* `PUT /api/produtos/{id}` - Atualiza produto
* `DELETE /api/produtos/{id}` - Remove produto
* `GET /api/produtos/{id}/melhores-ofertas` - Lista as melhores ofertas para o produto

### Fornecedor

* `GET /api/fornecedores` - Lista todos os fornecedores
* `GET /api/fornecedores/{id}` - Retorna fornecedor por ID
* `POST /api/fornecedores` - Cria novo fornecedor
* `PUT /api/fornecedores/{id}` - Atualiza fornecedor
* `DELETE /api/fornecedores/{id}` - Remove fornecedor

### Orçamento

* `GET /api/orcamentos` - Lista todos os orçamentos
* `GET /api/orcamentos/{id}` - Retorna orçamento com seus itens
* `POST /api/orcamentos` - Cria novo orçamento
* `PUT /api/orcamentos/{id}` - Atualiza orçamento
* `DELETE /api/orcamentos/{id}` - Remove orçamento
* `GET /api/orcamentos/por-fornecedor/{idFornecedor}` - Lista orçamentos de um fornecedor

### Item de Orçamento

* `POST /api/itens` - Cria item para um orçamento
* `PUT /api/itens/{id}` - Atualiza um item
* `DELETE /api/itens/{id}` - Remove item

---

## 📂 Como executar

### 1. Requisitos

* Java 17+
* Maven
* PostgreSQL

### 2. Criar o banco de dados

Crie um banco de dados PostgreSQL com o nome desejado. Exemplo:

```sql
CREATE DATABASE suprajavadb;
```

### 3. Configurar variáveis de ambiente (.env)

Na raiz do projeto, crie um arquivo chamado `.env` com o seguinte conteúdo:

```env
DB_URL=jdbc:postgresql://localhost:5432/suprajavadb
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

> Essas variáveis são carregadas no `main()` da classe `SuprajavaApplication.java` e usadas para configurar a conexão com o banco de dados.

### 4. Rodar o projeto

```bash
./mvnw spring-boot:run
```

Ao iniciar, o sistema carregará os menus no console via classe `MainConsoleView`.

---

## 📊 Estrutura principal do projeto

* `model`: entidades JPA do sistema
* `repository`: interfaces com queries personalizadas
* `dto`: transferência de dados entre camadas
* `controller`: endpoints REST
* `view`: classe principal de interação via terminal (CLI)

---

## ✅ Exemplos de uso via terminal

* Cadastrar produtos com unidade
* Criar orçamentos e adicionar itens com preços
* Listar os fornecedores que oferecem o menor preço para um produto
* Atualizar e excluir orçamentos e itens

