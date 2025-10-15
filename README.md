# Librarytest – API de Biblioteca (Spring Boot)

API REST para gerenciamento de usuários, livros e empréstimos de uma biblioteca. O projeto utiliza Spring Boot, Spring Data JPA, validação, Flyway para migrações e PostgreSQL como banco de dados.

## Sumário
- Visão Geral
- Tecnologias
- Requisitos
- Configuração do Ambiente
- Como Executar
- Migrações (Flyway)
- Endpoints
  - Usuários
  - Livros
  - Empréstimos
- Paginação e Filtros
- Erros Comuns / Troubleshooting
- Swagger / OpenAPI

## Visão Geral
Este projeto expõe endpoints para:
- Cadastrar, buscar, atualizar e deletar Usuários;
- Cadastrar, buscar, atualizar, deletar e listar Livros disponíveis;
- Realizar e devolver Empréstimos de livros por Usuário, além de consultar empréstimos e empréstimos em atraso por usuário.

## Tecnologias
- Java 17
- Spring Boot 4 (snapshot do starter parent)
- Spring Web (MVC)
- Spring Data JPA
- Bean Validation (Jakarta Validation)
- Flyway (migração de banco)
- PostgreSQL (driver e dialect)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven

## Requisitos
- Java 17+ instalado
- Maven 3.8+ (opcional, você pode usar ./mvnw ou mvnw.cmd do projeto)
- PostgreSQL em execução e acessível (por padrão em localhost:5432)

## Configuração do Ambiente
1) Crie o banco de dados no PostgreSQL (por padrão o nome é `biblioteca`):
   - Abra o psql e execute: `CREATE DATABASE biblioteca;`

2) Ajuste as credenciais em `src/main/resources/application.properties` se necessário:
```
spring.jpa.database=POSTGRESQL
spring.jpa.properties.hibernate.show-sql=true
spring.jpa.show-sql=true
spring.database.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=postgres
spring.datasource.password=123
server.error.include-stacktrace=never
```

3) As migrações do Flyway serão aplicadas automaticamente na inicialização (tabelas `usuarios`, `livros` e `emprestimos`).

## Como Executar
- Usando Maven Wrapper (recomendado):
  - Windows: `mvnw.cmd spring-boot:run`
  - Linux/Mac: `./mvnw spring-boot:run`

- Ou empacotar e executar o JAR:
  - `mvnw.cmd clean package` (ou `./mvnw clean package`)
  - `java -jar target/librarytest-0.0.1-SNAPSHOT.jar`

Por padrão, a aplicação sobe em `http://localhost:8080`.

## Migrações (Flyway)
As migrações estão em `src/main/resources/db/migration`:
- V1__create-table-usuario.sql
- V2__create-table-livro.sql
- V3__create-table-emprestimos.sql

Estruturas principais:
- usuarios(id, nome, email, matricula)
- livros(id, titulo, autor, isbn, quantidade)
- emprestimos(id, datas, usuario_id, livro_id)

## Endpoints
Abaixo um resumo dos principais endpoints. Os códigos de status mais comuns: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict.

### Usuários
Base: `/usuarios`

- POST `/usuarios`
  - Cadastra um usuário.
  - Body (JSON):
    {
      "nome": "João da Silva",
      "email": "joao@exemplo.com",
      "matricula": "MAT-001"
    }
  - Respostas: 201 Created.

- GET `/usuarios/{id}`
  - Busca usuário por id.
  - Resposta 200 com JSON do usuário.

- PATCH `/usuarios/{id}`
  - Atualiza dados do usuário.
  - Body (JSON): campos conforme UsuarioDTO
    {
      "nome": "João Updated",
      "email": "joaoupd@exemplo.com",
      "matricula": "MAT-001"
    }
  - Resposta 200 com usuário atualizado.

- DELETE `/usuarios/{id}`
  - Remove usuário.
  - Resposta 204 No Content.

- GET `/usuarios/page`
  - Busca paginada com filtros opcionais por `nome`, `email`, `matricula`.
  - Exemplo: `/usuarios/page?nome=joao&page=0&size=10`
  - Resposta 200 com Page<Usuario>.

### Livros
Base: `/livros`

- POST `/livros`
  - Cadastra um livro.
  - Body (JSON):
    {
      "titulo": "Clean Code",
      "autor": "Robert C. Martin",
      "isbn": "9780132350884",
      "quantidade": 5
    }
  - Resposta 201 Created.

- GET `/livros/{id}`
  - Busca livro por id.
  - Resposta 200 com JSON do livro.

- PATCH `/livros/{id}`
  - Atualiza dados do livro.
  - Body (JSON) conforme LivroDTO.
  - Resposta 200 com livro atualizado.

- DELETE `/livros/{id}`
  - Remove livro.
  - Resposta 204 No Content.

- GET `/livros`
  - Lista livros disponíveis (paginado).
  - Parâmetros de paginação padrão: `page`, `size`, `sort`.
  - Exemplo: `/livros?page=0&size=10`

### Empréstimos
Base: `/emprestimos`

- POST `/emprestimos?livroIds=1,2&usuarioId=1`
  - Realiza empréstimo de um ou mais livros para o usuário informado.
  - Resposta 201 Created.

- PATCH `/emprestimos?livroIds=1,2&usuarioId=1`
  - Devolve um ou mais livros emprestados pelo usuário informado.
  - Resposta 200 OK.

- GET `/emprestimos/usuario/{usuarioId}`
  - Lista os empréstimos do usuário.
  - Resposta 200 com lista de empréstimos.

- GET `/emprestimos/usuario/{usuarioId}/em-atraso`
  - Lista os empréstimos em atraso do usuário.
  - Resposta 200 com lista de empréstimos em atraso.

## Paginação e Filtros
- A paginação utiliza os parâmetros Spring padrão: `page`, `size`, `sort`.
- Exemplos:
  - `GET /livros?page=0&size=10&sort=titulo,asc`
  - `GET /usuarios/page?nome=ana&size=5`

## Erros Comuns / Troubleshooting
- Conexão recusada ao banco:
  - Verifique se o PostgreSQL está rodando e se `spring.datasource.url`, `username` e `password` estão corretos.
  - Garanta que o banco `biblioteca` exista.
- Falha nas migrações do Flyway:
  - Cheque se o usuário do banco tem permissões.
  - Verifique se não há tabelas pré-existentes conflitantes.
- Validações de payload:
  - `isbn`, `titulo`, `autor`, `matricula` possuem validações; revise mensagens de erro no retorno 400.

## Swagger / OpenAPI
A documentação automática via Swagger UI está disponível quando a aplicação está em execução:
- URL: `http://localhost:8080/swagger-ui/index.html`

Isso permite explorar e testar os endpoints diretamente no navegador.
