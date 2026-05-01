## CRT — Sistema de Gestão de Ações

Trabalho desenvolvido para a disciplina de **Sistemas da Informação** na **UniFEF**, sob orientação do professor **Jefferson**.

**Grupo:**
- Gabriel Pereira
- Rian Araújo
- Paulo Candido

---

## Sobre o projeto

O CRT é uma API REST desenvolvida em Java com Spring Boot que simula um sistema de gestão de ações do mercado financeiro. A ideia central foi criar algo que vai além de um simples CRUD — o sistema conversa com APIs externas reais para buscar dados de empresas, endereços e cotações de ações, tanto do mercado brasileiro quanto do americano.

Durante o desenvolvimento, nos preocupamos em organizar o código em camadas bem definidas e isolar as integrações externas usando interfaces, o que facilita muito a manutenção e a troca de fornecedores de API no futuro.

---

## Como rodar o projeto

**Pré-requisitos:**
- Java 17+
- Maven
- IntelliJ IDEA (ou qualquer IDE de sua preferência)

**Passos:**

1. Clone o repositório
2. Abra o projeto na IDE
3. Configure as chaves de API no `application.properties` (veja a seção abaixo)
4. Rode a classe principal
5. Acesse `http://localhost:8080`

O banco de dados utilizado é o **H2 em memória**, então não precisa configurar nada extra — sobe junto com a aplicação.

---

## Configuração das chaves de API

No arquivo `src/main/resources/application.properties`, adicione:

```properties
brapi.token=SEU_TOKEN_BRAPI
alphavantage.key=SUA_KEY_ALPHAVANTAGE
twelvedata.key=SUA_KEY_TWELVEDATA
```

Cada chave é gratuita e pode ser obtida nos sites listados na seção de APIs.

---

## APIs externas utilizadas

### 1. BrasilAPI — Consulta de CNPJ
- **Site:** https://brasilapi.com.br
- **Uso:** Buscamos os dados cadastrais da corretora (razão social, nome fantasia, CEP, UF) a partir do CNPJ informado.
- **Autenticação:** Não requer chave de API.
- **Limitações:** Pode apresentar instabilidade em horários de pico. Dados dependem da base da Receita Federal.

### 2. ViaCEP — Consulta de endereço
- **Site:** https://viacep.com.br
- **Uso:** A partir do CEP retornado pela consulta de CNPJ, buscamos o logradouro, bairro e cidade da corretora.
- **Autenticação:** Não requer chave de API.
- **Limitações:** Cobre apenas CEPs brasileiros. Alguns CEPs comerciais retornam campos vazios.

### 3. brapi.dev — Cotação de ações brasileiras
- **Site:** https://brapi.dev
- **Uso:** Consultamos a cotação atual de ações negociadas na B3 (ex: PETR4, VALE3) a partir do ticker informado.
- **Autenticação:** Requer token gratuito cadastrado no site.
- **Limitações:** Plano gratuito tem limite de requisições mensais. Cotações podem ter pequeno delay.

### 4. Twelve Data — Cotação de ações americanas
- **Site:** https://twelvedata.com
- **Uso:** Consultamos a cotação de ações negociadas na NYSE e NASDAQ (ex: AAPL, GOOGL).
- **Autenticação:** Requer chave de API gratuita.
- **Limitações:** Plano gratuito permite 8 requisições por minuto e 800 por dia.

### 5. Alpha Vantage — Cotação de ações americanas (fallback)
- **Site:** https://alphavantage.co
- **Uso:** Utilizado como alternativa ao Twelve Data quando este falha ou atinge o limite de requisições.
- **Autenticação:** Requer chave de API gratuita.
- **Limitações:** Plano gratuito permite 25 requisições por dia. Pode ter delay de resposta.

---

## Arquitetura do projeto

O projeto segue uma arquitetura em camadas, separando bem as responsabilidades de cada parte do sistema:

```
com.crt
├── adapter         # Integrações com APIs externas (isoladas por interfaces)
│   ├── cep
│   ├── cnpj
│   └── acao
├── controller      # Endpoints REST
├── dto             # Objetos de transferência de dados
├── entity          # Entidades do banco de dados
├── exception       # Tratamento centralizado de erros
├── repository      # Acesso ao banco de dados
└── service         # Regras de negócio
```

Uma decisão importante que tomamos foi usar **interfaces nos adapters** — por exemplo, `CepService`, `CnpjService` e `AcaoApiService`. Isso garante que, se precisarmos trocar uma API por outra, basta criar uma nova implementação sem mexer no resto do sistema. Esse é o padrão **Adapter**, aplicado justamente para isolar os serviços de terceiros.

---

## Endpoints disponíveis

### Corretoras

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/corretoras?cnpj={cnpj}` | Cadastra uma corretora pelo CNPJ |
| GET | `/corretoras` | Lista todas as corretoras |
| GET | `/corretoras/{id}` | Busca corretora por ID |
| GET | `/corretoras/cnpj/{cnpj}` | Busca corretora por CNPJ |

### Ações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/acoes?ticker={ticker}&mercado={mercado}` | Cadastra uma ação (mercado: BR ou EUA) |
| GET | `/acoes` | Lista todas as ações |
| GET | `/acoes/{id}` | Busca ação por ID |
| GET | `/acoes/ticker/{ticker}` | Busca ação por ticker |
| PUT | `/acoes/{id}/atualizar-cotacao` | Atualiza a cotação de uma ação |

---

## Tratamento de erros

O sistema possui tratamento centralizado de erros via `@RestControllerAdvice`. Todos os erros retornam um JSON padronizado:

```json
{
    "status": 400,
    "message": "CNPJ já cadastrado",
    "timestamp": "2026-04-28T21:00:00"
}
```

Cenários tratados:
- CNPJ já cadastrado
- Ticker já cadastrado
- Corretora não encontrada
- Ação não encontrada
- Falha na comunicação com APIs externas
- Mercado inválido (diferente de BR ou EUA)

---

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok
- RestTemplate
- Maven