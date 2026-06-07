<img width="1600" style="height:auto; border-radius: 12px;" alt="banner" src="../../images/banner.png" />

# Backend — Sistema de Mérito Digital

> API REST que sustenta o ciclo de reconhecimento e recompensa: autenticação de alunos, professores e empresas, distribuição de moedas, controle de saldo e extrato, gestão de vantagens e resgate com geração e validação de cupons, com notificações automáticas por e-mail.

---

## 🛠️ Stack Principal

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Testes-1565C0?style=for-the-badge&logo=h2&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Media-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Lombok](https://img.shields.io/badge/Lombok-Boilerplate-CC0000?style=for-the-badge&logo=lombok&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

---

## 📑 Sumário

- [Sobre a API](#-sobre-a-api)
- [Arquitetura em camadas](#-arquitetura-em-camadas)
- [Estrutura de pastas](#-estrutura-de-pastas)
- [Modelo de domínio](#-modelo-de-domínio)
- [Autenticação e segurança](#-autenticação-e-segurança)
- [Endpoints da API](#-endpoints-da-api)
- [Estratégia de persistência](#-estratégia-de-persistência)
- [Notificações por e-mail](#-notificações-por-e-mail)
- [Upload de imagens](#-upload-de-imagens)
- [Variáveis de ambiente](#-variáveis-de-ambiente)
- [Instalação e execução](#-instalação-e-execução)
- [Tecnologias e dependências](#-tecnologias-e-dependências)

---

## 📖 Sobre a API

Esta API REST, desenvolvida em **Java 21 + Spring Boot**, é o núcleo do **Sistema de Mérito Digital**. Ela concentra toda a regra de negócio do ecossistema:

- **Autenticação e cadastro** de alunos, professores e empresas parceiras, com emissão de tokens **JWT**;
- **Distribuição de moedas** de professores para alunos, sempre acompanhada de uma mensagem de reconhecimento e validação de saldo;
- **Controle de saldo e extrato** consolidado de transações (envios, recebimentos e resgates) por participante;
- **Gestão de vantagens** cadastradas por empresas parceiras, com upload de imagem via Cloudinary;
- **Resgate de vantagens**, com débito automático de saldo, geração de **cupom único** e envio de e-mails de confirmação para aluno e empresa, incluindo fluxo de **reembolso automático** em caso de cupom inválido.

---

## 🏛️ Arquitetura em camadas

A API segue o estilo **MVC em camadas**, isolando regras de negócio de detalhes de transporte (HTTP) e persistência (JPA/MySQL):

```
┌──────────────────────────────────────────────────────────────┐
│                        Controller                            │
│   Endpoints REST · validação de entrada (@Valid) · DTOs      │
└────────────────────────────┬─────────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────────┐
│                          Service                             │
│   Regras de negócio · transações (@Transactional)            │
│   exceções de domínio · orquestração entre repositories      │
└──────┬───────────────┬───────────────┬───────────────┬───────┘
       │               │               │               │
  Repository        Mapper        EmailService    ImagemService
  (Spring Data    (entidade ↔       (SMTP /         (Cloudinary —
   JPA / MySQL)       DTO)        Spring Mail)      upload/remoção)
       │
   Model (entidades JPA com herança: Usuario, Participante,
          Aluno, Professor, Empresa, Transacao, Distribuicao, Resgate...)
```

**Decisões de arquitetura:**

| Decisão | Detalhe |
|---|---|
| Herança de entidades JPA | `Usuario` (abstrata) → `Participante` (abstrata) → `Aluno` / `Professor`; `Usuario` → `Empresa`; `Transacao` (abstrata) → `Distribuicao` / `Resgate` |
| DTOs dedicados de entrada/saída | Pacote `dto/` separa `*Request` (entrada validada com Bean Validation) de `*Response`/`*ResumoResponse`/`*CompletoResponse` (saída), evitando vazamento de entidades JPA |
| Mapeamento manual | Pacote `mapper/` converte entidade ↔ DTO (`AlunoMapper`, `EmpresaMapper`, `ProfessorMapper`, `VantagemMapper`, `ResgateMapper`) |
| Exceções de domínio | Uma exceção por contexto (`AlunoException`, `EmpresaException`, `ProfessorException`, `DistribuicaoException`, `ResgateException`, `VantagemException`, `ParticipanteException`, `InstituicaoException`, `JwtAuthenticationException`) tratadas centralmente pelo `GlobalExceptionHandler` |
| Seed inicial | `DataInitializer` cadastra instituições, professores e saldos iniciais na primeira subida da aplicação |
| Stateless por design | `SecurityConfig` define `SessionCreationPolicy.STATELESS` — toda autenticação é resolvida via token JWT a cada requisição |

---

## 📁 Estrutura de pastas

```
api/
├── src/
│   ├── main/
│   │   ├── java/com/projeto/lab/implementacao/
│   │   │   ├── Main.java                  # Classe de inicialização (Spring Boot)
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java        # Filtro JWT, CORS, política stateless, regras de acesso
│   │   │   │   ├── SwaggerConfig.java         # Configuração do Swagger / OpenAPI
│   │   │   │   ├── CloudinaryConfig.java      # Bean do client Cloudinary
│   │   │   │   ├── DataInitializer.java       # Seed de instituições, professores e saldos
│   │   │   │   └── filter/
│   │   │   │       ├── JwtAuthenticationFilter.java   # Intercepta e valida o Bearer token
│   │   │   │       └── JwtAuthenticationToken.java    # Token de autenticação populado no SecurityContext
│   │   │   ├── controller/                # Endpoints REST (Auth, Aluno, Professor, Empresa, Vantagem, Distribuicao, Resgate, Participante)
│   │   │   ├── dto/                       # Records de request/response (entrada validada e saída desacoplada da entidade)
│   │   │   ├── exception/                 # Exceções de domínio + GlobalExceptionHandler
│   │   │   ├── mapper/                    # Conversão entidade ↔ DTO
│   │   │   ├── model/                     # Entidades JPA e hierarquias de herança
│   │   │   ├── repository/                # Interfaces Spring Data JPA
│   │   │   └── service/                   # Regras de negócio, transações e orquestração
│   │   └── resources/
│   │       └── application.properties     # Configuração de datasource, JPA, JWT, Cloudinary e e-mail (via variáveis de ambiente)
│   └── test/java/                         # Testes (Spring Boot Test + Spring Security Test)
├── pom.xml
├── mvnw / mvnw.cmd
└── .env                                   # Variáveis de ambiente locais (nunca versionar)
```

---

## 🧬 Modelo de domínio

A modelagem usa herança de entidades JPA para representar os diferentes perfis de usuário e tipos de transação de forma coesa:

| Entidade | Extende | Principais atributos |
|---|---|---|
| `Usuario` *(abstrata)* | — | `id`, `email`, `senha`, `nome` |
| `Participante` *(abstrata)* | `Usuario` | `saldoMoedas`, `cpf`, `instituicao`, `transacoesComoPagador`, `transacoesComoRecebedor` |
| `Aluno` | `Participante` | `rg`, `endereco`, `curso` |
| `Professor` | `Participante` | `departamento`, `ultimaAtualizacaoSaldo` |
| `Empresa` | `Usuario` | `cnpj`, `endereco`, `vantagens` |
| `Instituicao` | — | `nome`, `cnpj`, `endereco`, `participantes` |
| `Vantagem` | — | `nome`, `descricao`, `custo`, `imagem`, `estoque`, `disponivel`, `empresa` |
| `Transacao` *(abstrata)* | — | `codigo`, `data`, `valor`, `pagador`, `recebedor` |
| `Distribuicao` | `Transacao` | `motivo` (mensagem de reconhecimento — RN3) |
| `Resgate` | `Transacao` | `cupom`, `utilizado`, `vantagem` |

> Consulte o [Diagrama de Classes](/projeto/DiagramaDeClasse.drawio.svg), o [Diagrama Entidade-Relacionamento](/projeto/DiagramaER.drawio.png) e o [Modelo ER](/projeto/ModeloER.drawio.svg) no [README raiz](/README.md#%EF%B8%8F-modelagem) para a visão completa da modelagem.

---

## 🔐 Autenticação e segurança

A API utiliza **Spring Security** com autenticação **stateless baseada em JWT**:

```
Cliente                         API
  │  POST /auth/login (email, senha)
  ├──────────────────────────────────▶│  UsuarioService.authenticate()
  │                                    │  valida credenciais (BCrypt)
  │◀── 200 OK { token: "<JWT>" } ──────┤  JwtService.generateToken()
  │                                    │  claims: id, email, role, exp (24h)
  │
  │  GET /participantes/{id}/extrato
  │  Authorization: Bearer <JWT>
  ├──────────────────────────────────▶│  JwtAuthenticationFilter
  │                                    │  extrai e valida o token
  │                                    │  popula o SecurityContext (JwtAuthenticationToken)
  │◀── 200 OK { ...extrato } ──────────┤  Controller → Service → Repository
```

**Pontos-chave:**

- O **papel (`role`)** do usuário é derivado da própria classe da entidade (`Aluno`, `Professor` ou `Empresa`) e embutido como *claim* no token, permitindo autorização baseada em perfil tanto na API quanto no front-end (`@EnableMethodSecurity` habilitado).
- **Senhas** são armazenadas com hash **BCrypt** (`PasswordEncoder`).
- **CORS** é configurado para aceitar as origens de desenvolvimento (`localhost:3000`, `localhost:5173`, `localhost:8080`) com suporte a credenciais.
- **Endpoints públicos** (sem necessidade de token): `/auth/**`, `/alunos/cadastro`, `/empresas/cadastro`, `/swagger-ui/**` e `/v3/api-docs/**`. Todas as demais rotas exigem um Bearer token válido (`anyRequest().authenticated()`).
- O `JwtAuthenticationFilter` é executado antes do `UsernamePasswordAuthenticationFilter` e nunca é contornado — endpoints públicos são declarados explicitamente no `SecurityConfig`.

---

## 🌐 Endpoints da API

> Documentação interativa (Swagger UI) disponível em `http://localhost:8080/swagger-ui.html` após subir a aplicação. Rotas marcadas com 🔓 são públicas; as demais exigem `Authorization: Bearer <token>`.

### Autenticação (`/auth`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` 🔓 | Autentica usuário (e-mail + senha) e retorna um token JWT |

### Alunos (`/alunos`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/alunos/cadastro` 🔓 | Cadastra um novo aluno (vinculado a uma instituição/curso) e retorna o token JWT |
| `GET` | `/alunos/{id}/completo` | Retorna os dados completos de um aluno |
| `GET` | `/alunos/{id}/resumo` | Retorna um resumo dos dados de um aluno |
| `GET` | `/alunos/resumo` | Lista todos os alunos em formato resumido |
| `PUT` | `/alunos/{id}` | Atualiza os dados de um aluno |
| `DELETE` | `/alunos/{id}` | Remove um aluno |

### Professores (`/professores`)

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/professores/{id}` | Retorna os dados completos de um professor |
| `GET` | `/professores` | Lista todos os professores em formato resumido |
| `PUT` | `/professores/{id}` | Atualiza os dados de um professor |
| `DELETE` | `/professores/{id}` | Remove um professor |

### Empresas (`/empresas`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/empresas/cadastro` 🔓 | Cadastra uma nova empresa parceira e retorna o token JWT |
| `GET` | `/empresas/{id}/completo` | Retorna os dados completos de uma empresa |
| `GET` | `/empresas/{id}/resumo` | Retorna um resumo dos dados de uma empresa |
| `GET` | `/empresas/resumo` | Lista todas as empresas em formato resumido |
| `PUT` | `/empresas/{id}` | Atualiza os dados de uma empresa |
| `DELETE` | `/empresas/{id}` | Remove uma empresa |

### Vantagens (`/vantagens`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/vantagens` | Cadastra uma vantagem (multipart: dados + imagem opcional, upload via Cloudinary) |
| `GET` | `/vantagens/{id}` | Busca uma vantagem por ID |
| `GET` | `/vantagens` | Lista todas as vantagens cadastradas |
| `GET` | `/vantagens/custo-maximo?custoMaximo=` | Lista vantagens com custo até o valor informado |
| `PUT` | `/vantagens/{id}` | Atualiza uma vantagem (dados e/ou imagem) |
| `DELETE` | `/vantagens/{id}` | Remove uma vantagem |

### Distribuições (`/distribuicoes`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/distribuicoes` | Professor distribui moedas a um aluno com valor e mensagem de reconhecimento (valida saldo do professor — RN2) |

### Resgates (`/resgates`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/resgates` | Aluno resgata uma vantagem: valida saldo, debita moedas, gera cupom único e dispara e-mails para aluno e empresa |
| `GET` | `/resgates/aluno/{alunoId}` | Lista os resgates feitos por um aluno |
| `GET` | `/resgates/{resgateId}/vantagem` | Retorna os detalhes da vantagem associada a um resgate |
| `POST` | `/resgates/resgatar/vantagem` | Valida o cupom apresentado pelo aluno na empresa; se inválido, processa reembolso automático |

### Participantes (`/participantes`)

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/participantes/{id}/saldo` | Consulta o saldo de moedas de um participante (aluno ou professor) |
| `GET` | `/participantes/{id}/extrato` | Retorna o extrato consolidado e agrupado de transações de um participante |

---

## 🗄️ Estratégia de persistência

A API utiliza **Spring Data JPA** sobre **MySQL** (com **H2** disponível para testes), seguindo o padrão Repository:

- As classes do modelo de domínio (`model/`) são mapeadas para o banco com `@Entity`, incluindo as hierarquias de herança descritas acima.
- A camada `repository/` define o contrato de acesso aos dados através de interfaces `JpaRepository` (`AlunoRepository`, `TransacaoRepository`, `VantagemRepository`, `ResgateRepository`, etc).
- A camada `service/` consome os repositories, aplica as regras de negócio e gerencia transações com `@Transactional`, garantindo que operações compostas — como **debitar saldo + registrar transação + gerar cupom** no resgate, ou **debitar saldo do professor + creditar o aluno** na distribuição — ocorram de forma atômica.
- A camada `controller/` consome exclusivamente os services, nunca acessando repositories diretamente, e troca dados com o cliente por meio de DTOs convertidos pelos `mapper`s.
- `spring.jpa.hibernate.ddl-auto=update` mantém o schema sincronizado com as entidades durante o desenvolvimento, e o `DataInitializer` popula instituições, professores e saldos iniciais (1.000 moedas/semestre — RN1) na primeira execução.

---

## ✉️ Notificações por e-mail

O `EmailService` (Spring Boot Starter Mail) automatiza toda a comunicação por e-mail prevista nas regras de negócio (RF5, RF8, RNF2):

| Evento | Notificação enviada |
|---|---|
| Professor distribui moedas | E-mail ao aluno informando o valor recebido, o motivo/mensagem de reconhecimento e o professor responsável |
| Aluno resgata uma vantagem | E-mail com o **cupom de resgate** para o aluno e e-mail de notificação com o código de conferência para a **empresa parceira** (RN7) |
| Cupom inválido apresentado na troca | E-mail automático de **reembolso**, devolvendo o valor da vantagem ao saldo do aluno |

---

## 🖼️ Upload de imagens

O cadastro e a edição de vantagens aceitam upload de imagem via **multipart/form-data**. O `ImagemService`, configurado pelo `CloudinaryConfig`, envia o arquivo para o **Cloudinary** (`resource_type: auto`, organizado por pastas) e armazena apenas a **URL segura** retornada na entidade `Vantagem` — o binário da imagem nunca trafega pelo banco de dados. Ao remover ou substituir uma vantagem, a imagem correspondente também é removida do Cloudinary.

---

## 🔑 Variáveis de ambiente

Crie um arquivo `.env` na raiz de `implementacao/api/` (carregado via `spring-dotenv`) com as variáveis abaixo. **Nunca versionar em produção.**

```dotenv
# ── Banco de Dados (MySQL) ─────────────────────
DB_HOST=localhost
DB_PORT=3306
DB_DATABASE=merito_digital
DB_USERNAME=root
DB_PASSWORD=sua_senha
DB_DRIVER=com.mysql.cj.jdbc.Driver

# ── Segurança JWT ──────────────────────────────
JWT_SECRET=chave_jwt_minimo_256_bits

# ── Cloudinary (upload de imagens das vantagens) ─
CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=sua_api_secret

# ── E-mail (Spring Mail / SMTP) ────────────────
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS_ENABLE=true
```

---

## 🚀 Instalação e execução

### Pré-requisitos

- Java 21+
- Maven 3.9+ (ou utilize o wrapper `./mvnw` incluído no projeto)
- MySQL 8 em execução (local ou remoto)
- Conta Cloudinary e servidor SMTP configurados (para upload de imagens e envio de e-mails)

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/marcosffp/Sistema-Merito-Digital.git
cd Sistema-Merito-Digital/implementacao/api

# 2. Crie e preencha o arquivo .env (ver seção "Variáveis de ambiente")

# 3. Suba a aplicação (cria o schema automaticamente via ddl-auto=update)
./mvnw spring-boot:run
```

### Acessos após subir

| Serviço | URL | Observação |
|---|---|---|
| API REST | `http://localhost:8080` | Autenticação via JWT Bearer token |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | Documentação interativa dos endpoints |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` | Especificação OpenAPI da API |

### Build e testes

```bash
# Build completo
./mvnw clean package

# Executar testes (Spring Boot Test + Spring Security Test, com H2 em memória)
./mvnw test
```

---

## 📦 Tecnologias e dependências

| Categoria | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java | 21 |
| Framework | Spring Boot (parent) | 3.5.6 |
| Persistência | Spring Data JPA + Hibernate | — |
| Banco relacional | MySQL (`mysql-connector-j`) | runtime |
| Banco em memória (testes) | H2 | runtime |
| Segurança | Spring Security + BCrypt | — |
| Autenticação | JJWT (`jjwt-api`/`impl`/`jackson`) | 0.11.5 |
| Autenticação (alternativa) | java-jwt (Auth0) | 4.4.0 |
| Variáveis de ambiente | spring-dotenv | 4.0.0 |
| Documentação de API | springdoc-openapi-starter-webmvc-ui | 2.8.9 |
| Upload de imagens | Cloudinary (`cloudinary-http44`) | 1.34.0 |
| E-mail | spring-boot-starter-mail | — |
| Boilerplate | Lombok | optional |
| Testes | spring-boot-starter-test + spring-security-test | — |
| Build | Maven (wrapper `mvnw`) | 3.9+ |

---

<div align="center">
  <img width="70%" alt="pucminas" src="../../images/banner-institucional.svg"/>
</div>
<p align="center">Fonte do banner: <a href="https://github.com/joaopauloaramuni">João Paulo Carneiro Aramuni</a></p>
