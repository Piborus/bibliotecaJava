### librarytest — API de Biblioteca (Spring Boot)

API para gerenciamento de biblioteca construída com Spring Boot 4 (snapshot), Java 17, JPA/Hibernate, validação e documentação via OpenAPI/Swagger. Inclui infraestrutura Docker (Dockerfile + docker-compose) e migrações via Flyway.

---

### Principais recursos
- Cadastro e consulta de livros e usuários
- Empréstimo e devolução de livros
- Consulta de empréstimos por usuário e em atraso
- Validações (Bean Validation) e tratamento global de erros (`GlobalExpetionHandler`)
- Migrações de banco com Flyway
- Documentação da API com Swagger UI

---

### Tecnologias
- Java 17
- Spring Boot (webmvc, data-jpa, validation)
- PostgreSQL
- Flyway
- Lombok
- springdoc-openapi
- Docker e Docker Compose

---

### Requisitos
- Java 17 (ou Docker, se for rodar tudo conteinerizado)
- Maven 3.9+ (se for build local)
- Docker Desktop com Docker Compose v2

---

### Executando com Docker (recomendado)

#### 1) Build e subir os serviços
```powershell
# na raiz do projeto
docker compose build
docker compose up -d
```

- App: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Postgres (a partir do host): ver seção “Portas do Postgres”.

#### 2) Logs
```powershell
docker compose logs -f db
# em outro terminal
docker compose logs -f app
```

#### 3) Parar e remover
```powershell
docker compose down          # mantém volume de dados
# ou
docker compose down -v       # remove volume (zera o banco)
```

---

### Portas do Postgres (atenção)
- Dentro da rede do Compose, a aplicação acessa o banco por `jdbc:postgresql://db:5432/biblioteca` (fixo no container: `5432`).
- Para o host Windows, publique uma porta livre. Exemplos válidos em `docker-compose.yml`:
  - Padrão (se a 5432 estiver livre):
    ```yaml
    ports:
      - "5432:5432"
    ```
  - Se a 5432 do host estiver ocupada (recomendado):
    ```yaml
    ports:
      - "15432:5432"
    ```

Nota: Evite mapear `5433:5433` para o Postgres, pois o Postgres dentro do container escuta em `5432`. O correto é `HOST:5432`.

---

### Executando localmente (sem Docker)
1) Ajuste `src\main\resources\application.properties` para apontar para seu Postgres local (padrão já é `localhost:5432`):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=postgres
spring.datasource.password=123
```
2) Suba o Postgres localmente e crie o banco `biblioteca`.
3) Build e run:
```powershell
mvn clean package -DskipTests
java -jar target\librarytest-0.0.1-SNAPSHOT.jar
```
- App: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

---

### Variáveis de ambiente úteis (Docker)
O serviço `app` aceita as seguintes variáveis (já definidas no `docker-compose.yml`):
- `SPRING_DATASOURCE_URL` (ex.: `jdbc:postgresql://db:5432/biblioteca`)
- `SPRING_DATASOURCE_USERNAME` (ex.: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` (ex.: `123`)
- `SERVER_PORT` (ex.: `8080`)
- `SPRING_JPA_SHOW_SQL` e `SPRING_JPA_PROPERTIES_HIBERNATE_SHOW_SQL` para logs de SQL

O container da aplicação também aceita `JAVA_OPTS` (veja `Dockerfile`) para tunar a JVM:
```bash
JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

---

### Endpoints principais (exemplos)
Consulte a documentação completa no Swagger UI. Exemplos relacionados a empréstimos:

- `POST /emprestimos` — Efetuar empréstimo
  - Parâmetros: `livroIds` (lista de `Long`), `usuarioId` (`Long`) via `query param`.
  - Resposta: `201 Created`

- `PATCH /emprestimos` — Devolver livros
  - Parâmetros: `livroIds` (lista), `usuarioId` via `query param`.
  - Resposta: `200 OK`

- `GET /emprestimos/usuario/{usuarioId}` — Empréstimos do usuário
  - Resposta: `200 OK` com `List<EmprestimoResumoDTO>`

- `GET /emprestimos/usuario/{usuarioId}/em-atraso` — Empréstimos em atraso do usuário
  - Resposta: `200 OK` com `List<EmprestimoResumoDTO>`

Os demais recursos (livros, usuários) também estão expostos e podem ser explorados pelo Swagger.

---

### Tratamento de erros
Erros são padronizados por `GlobalExpetionHandler`, cobrindo casos como:
- `404 Not Found` (recurso não encontrado)
- `400 Bad Request` (validação, parâmetros inválidos, tipos incompatíveis, JSON malformado)
- `409 Conflict` (violação de integridade — `DataIntegrityViolationException`)
- `500 Internal Server Error` (falhas inesperadas)

Formato de resposta: `ErroResponse { status, error, message }`.

---

### Migrações de banco (Flyway)
- As migrações são executadas automaticamente na inicialização.
- Certifique-se de que o banco `biblioteca` exista e que as credenciais estejam corretas.

---

### Executando via Maven (sem Docker)
```powershell
mvn clean package -DskipTests
java -jar target\librarytest-0.0.1-SNAPSHOT.jar
```

---

### Comandos úteis (Docker)
```powershell
# rebuild ignorando cache
docker compose build --no-cache

# reiniciar apenas o app
docker compose restart app

# logs do app
docker compose logs -f app

# conectar no banco do container (psql dentro do container)
docker exec -it librarytest-db psql -U postgres -d biblioteca -c "\\dt"
```

---

### Solução de problemas
- Porta 5432 ocupada:
  - Altere a publicação para `15432:5432` no `docker-compose.yml` e rode `docker compose up -d --build`, ou
  - Libere a `5432` no host (procure processo com `netstat -ano | findstr :5432`).

- Porta 8080 ocupada:
  - Troque para `8081:8080` no serviço `app` e rode `docker compose up -d --build`.

- Aviso `the attribute version is obsolete`:
  - O Compose v2 ignora `version:`. Remova a linha `version: "3.8"` para evitar o aviso (opcional).

---

### Estrutura (resumo)
- `src\main\java\br\com\haroldomorais\librarytest\controller` — controladores REST (ex.: `EmprestimoController`)
- `src\main\resources\application.properties` — configuração da aplicação
- `Dockerfile` — build multi-stage para imagem da aplicação
- `docker-compose.yml` — orquestração do app + Postgres

---

### Licença
Projeto para testes/estudos. Ajuste a licença conforme necessidade.

---

### Autor
`br.com.haroldomorais` — Haroldo Morais
