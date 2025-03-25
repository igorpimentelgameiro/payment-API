# API de Pagamento

## Descrição

Esta API foi projetada para gerenciar o processamento de pagamentos de débitos de pessoas físicas e jurídicas. Com ela, é possível registrar pagamentos, atualizar seus status, filtrar e listar pagamentos com base em critérios específicos e realizar exclusão lógica de pagamentos. Ela foi desenvolvida utilizando **Spring Boot** e **Java 17**, com **H2** como banco de dados embutido.

## Funcionalidades

A API oferece as seguintes funcionalidades:

### 1. **Receber Pagamento**

A API permite o recebimento de pagamentos com os seguintes dados:

- **Código do Débito**: Valor inteiro que representa o código único do débito.
- **CPF ou CNPJ do Pagador**: Identificação do pagador (pode ser CPF ou CNPJ).
- **Método de Pagamento**: Define o método utilizado para o pagamento. Pode ser um dos seguintes:
  - **Boleto**
  - **Pix**
  - **Cartão de Crédito**
  - **Cartão de Débito**
- **Número do Cartão**: Este campo é necessário apenas quando o método de pagamento for **Cartão de Crédito** ou **Cartão de Débito**.
- **Valor do Pagamento**: O valor monetário a ser pago.

O pagamento será armazenado no banco de dados com o status **"Pendente de Processamento"** inicialmente.

### 2. **Atualizar Status de Pagamento**

A API permite a atualização do status de um pagamento. Os status possíveis são:

- **Pendente de Processamento** (Status inicial ao criar um pagamento)
- **Processado com Sucesso**
- **Processado com Falha**

Regras de atualização de status:
- Quando o status for **"Processado com Sucesso"**, não é possível alterá-lo.
- Quando o status for **"Processado com Falha"**, ele pode ser alterado para **"Pendente de Processamento"**, mas não pode ser alterado para **"Processado com Sucesso"**.

### 3. **Listar Pagamentos**

A API permite listar todos os pagamentos recebidos com a possibilidade de **filtrar os pagamentos** com base nos seguintes critérios:

- **Código do Débito**
- **CPF/CNPJ do Pagador**
- **Status do Pagamento**

Esses filtros são úteis para consultar pagamentos específicos de forma eficiente.

### 4. **Exclusão Lógica de Pagamento**

A API oferece a funcionalidade de **exclusão lógica**, que altera o status do pagamento para **"Inativo"**. No entanto, essa alteração só pode ser realizada se o status do pagamento for **"Pendente de Processamento"**.

### Exemplo de Exclusão Lógica:

Se o pagamento estiver no status **"Pendente de Processamento"**, ele pode ser marcado como **"Inativo"** para fins de exclusão lógica, mantendo os registros no banco de dados para rastreabilidade.

---

## Tecnologias Utilizadas

- **Spring Boot** com **Java 17**.
- **Banco de Dados H2** embutido no Spring Boot (para fins de desenvolvimento e testes).
- **REST API** para interação com os dados via HTTP.
- **JSON** como formato de entrada e saída para os payloads da API.

---

## 📘 Endpoints da API

Abaixo estão listados todos os endpoints disponíveis na API, suas descrições, métodos HTTP e parâmetros esperados.

### 🔹 1. Criar Pagamento

- **Método**: `POST`
- **Endpoint**: `/criarPagamento`
- **Descrição**: Cria um novo pagamento. O número do cartão só deve ser informado se o método de pagamento for **cartão de crédito** ou **cartão de débito**.
- **Request Body**:
  ```json
  {
    "codigoDebito": 1001,
    "documento": "12345678900",
    "metodoPagamento": "CARTAO_CREDITO",
    "numeroCartao": "4111111111111111",
    "valorPagamento": 150.00
  }
  ```
- **Response**: `201 Created` com os dados detalhados do pagamento criado.

---

### 🔹 2. Listar Pagamentos com Filtro

- **Método**: `GET`
- **Endpoint**: `/listaPagamento`
- **Descrição**: Lista todos os pagamentos registrados, com possibilidade de aplicar filtros.
- **Parâmetros de Query (opcionais)**:
  - `codigoDebito` (Integer)
  - `cpfCnpj` (String)
  - `status` (Enum: `PENDENTE_PROCESSAMENTO`, `PROCESSADO_SUCESSO`, `PROCESSADO_FALHA`, `INATIVO`)
- **Exemplo de Requisição**:
  ```
  GET /listaPagamento?cpfCnpj=12345678900&status=PENDENTE_PROCESSAMENTO
  ```

---

### 🔹 3. Atualizar Status de Pagamento

- **Método**: `PUT`
- **Endpoint**: `/atualizarPagamento`
- **Descrição**: Atualiza o status de um pagamento existente.
- **Request Body**:
  ```json
  {
    "codigoPagamento": 1,
    "novoStatus": "PROCESSADO_SUCESSO"
  }
  ```
- **Regras de Negócio**:
  - Pagamentos com status **PROCESSADO_SUCESSO** não podem ser alterados.
  - Pagamentos com status **PROCESSADO_FALHA** só podem ser atualizados para **PENDENTE_PROCESSAMENTO**.

---

### 🔹 4. Desativar Pagamento (Exclusão Lógica)

- **Método**: `DELETE`
- **Endpoint**: `/desativarPagamento`
- **Descrição**: Marca um pagamento como **INATIVO**, desde que seu status atual seja **PENDENTE_PROCESSAMENTO**.
- **Parâmetros de Query**:
  - `codigoDebito` (Integer)

- **Exemplo**:
  ```
  DELETE /desativarPagamento?codigoDebito=1001
  ```

---

### 🔹 5. Detalhar Pagamento

- **Método**: `GET`
- **Endpoint**: `/detalharPagamento`
- **Descrição**: Retorna os detalhes completos de um pagamento específico.
- **Parâmetros de Query**:
  - `codigoPagamento` (Integer)

- **Exemplo**:
  ```
  GET /detalharPagamento?codigoPagamento=1
  ```

---

Todos os endpoints utilizam **JSON** para requisição e resposta. A validação é aplicada automaticamente em todos os payloads através da anotação `@Valid`.

---

## Como Rodar a Aplicação

### Requisitos

- **Java 17**
- **Maven** ou **Gradle** para gerenciamento de dependências

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/pagamento-api.git
   ```

2. Navegue até a pasta do projeto:
   ```bash
   cd pagamento-api
   ```

3. Execute a aplicação com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. A API estará rodando em `http://localhost:8080`.

---

## Considerações Finais

Esta API foi projetada para ser simples, porém funcional, com foco em receber e processar pagamentos. O uso de **Spring Boot** e **H2** permite um desenvolvimento rápido, com a possibilidade de expandir facilmente a aplicação com mais funcionalidades, como integração com gateways de pagamento, autenticação de usuários, entre outras.

---

**Desenvolvido por**: Igor Pimentel Gameiro
