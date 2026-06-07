<img width="1600" style="height:auto; border-radius: 12px;" alt="banner" src="images/banner.png" />

# Sistema de Mérito Digital

> Plataforma de gamificação do reconhecimento acadêmico: professores distribuem moedas digitais para reconhecer o bom desempenho e a participação dos alunos, que podem trocá-las por vantagens oferecidas por empresas parceiras das instituições de ensino.

---

## 🛠️ Stack Principal

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-7-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![React Router](https://img.shields.io/badge/React_Router-7-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-1.13-5A29E4?style=for-the-badge&logo=axios&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Media-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---

## 📑 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [Arquitetura](#%EF%B8%8F-arquitetura)
- [Estrutura do repositório](#-estrutura-do-repositório)
- [Histórias de usuário](#-histórias-de-usuário)
- [Requisitos](#-requisitos)
- [Regras de negócio](#-regras-de-negócio)
- [Modelagem](#-modelagem)
- [Estratégia de acesso a dados](#-definição-e-implementação-da-estratégia-de-acesso-ao-banco-de-dados)
- [Como executar](#-como-executar)
- [Tecnologias e dependências](#-tecnologias-e-dependências)

---

## 📖 Sobre o projeto

O **Sistema de Mérito Digital** é uma plataforma web que conecta três perfis — **alunos**, **professores** e **empresas parceiras** — em torno de um ciclo de reconhecimento e recompensa:

1. Cada **professor** recebe periodicamente um saldo de moedas digitais e as distribui aos **alunos** sempre acompanhadas de uma mensagem de reconhecimento, registrando o motivo do reconhecimento (bom desempenho, participação em sala, projetos, etc.).
2. O **aluno** acumula essas moedas, acompanha seu saldo e extrato de transações, e pode trocá-las por **vantagens** (descontos, brindes, cursos, produtos) cadastradas por **empresas parceiras** das instituições de ensino.
3. Ao resgatar uma vantagem, o sistema gera um **cupom único** que é enviado por e-mail tanto ao aluno quanto à empresa parceira, permitindo a conferência e validação da troca no momento do uso.

Todo o ciclo — distribuição de moedas, consulta de extrato, cadastro/gestão de vantagens e resgate — é automatizado, autenticado via **JWT** e notificado por **e-mail**, criando um ecossistema de incentivo positivo dentro do ambiente acadêmico.

---

## 🏛️ Arquitetura

O projeto é dividido em dois módulos independentes que se comunicam via **API REST**: um back-end em **Spring Boot** (arquitetura em camadas / MVC) e um front-end em **React + Vite** (SPA consumindo a API com Axios).

```
┌──────────────────────────────┐        REST/JSON + JWT        ┌────────────────────────────────────┐
│         Front-end            │  ───────────────────────────▶ │             Back-end               │
│   React 19 · Vite · Router   │                                │     Spring Boot · Spring Security  │
│                              │ ◀─────────────────────────────│                                    │
│  Pages → Services → Axios    │                                │  Controller → Service → Repository │
└──────────────────────────────┘                                └─────────────────┬──────────────────┘
                                                                                   │
                                                          ┌────────────────────────┼────────────────────────┐
                                                          │                        │                        │
                                                     MySQL/JPA               Cloudinary               SMTP / Spring Mail
                                                  (persistência)        (upload de imagens          (notificações por
                                                                          das vantagens)              e-mail e cupons)
```

**Padrões e decisões centrais do back-end:**

| Padrão / decisão | Onde se aplica |
|---|---|
| Arquitetura em camadas (MVC) | `Controller` → `Service` → `Repository` → `Model`, com `Mapper`/`DTO` isolando a entrada e saída da API |
| Herança de entidades JPA | `Usuario` → `Participante` → `Aluno`/`Professor`; `Usuario` → `Empresa`; `Transacao` → `Distribuicao`/`Resgate` |
| Autenticação stateless via JWT | `JwtAuthenticationFilter` valida o token em cada requisição; sessão `STATELESS` no `SecurityConfig` |
| DTOs e mapeamento dedicado | Pacotes `dto/` e `mapper/` evitam expor entidades JPA diretamente nas respostas da API |
| Tratamento centralizado de erros | `GlobalExceptionHandler` + exceções de domínio (`AlunoException`, `ResgateException`, `VantagemException`, etc.) |
| Geração de cupom único | `ResgateService` gera código alfanumérico para conferência da troca por aluno e empresa |
| Notificação assíncrona por e-mail | `EmailService` envia confirmações de recebimento de moedas, cupons de resgate e reembolsos |
| Upload de mídia externo | `ImagemService` + `CloudinaryConfig` armazenam as imagens das vantagens fora do banco de dados |

---

## 📁 Estrutura do repositório

```
Sistema-Merito-Digital/
├── docs/                      # Documento de especificação do projeto (PDF)
├── images/                    # Banner e identidade visual do README
├── projeto/                   # Diagramas UML (casos de uso, classes, sequência, comunicação, ER...)
└── implementacao/
    ├── api/                   # Back-end — Spring Boot (Java 21)
    │   ├── src/main/java/com/projeto/lab/implementacao/
    │   │   ├── config/        # Segurança, JWT, Cloudinary, Swagger, seed de dados (DataInitializer)
    │   │   ├── controller/    # Endpoints REST
    │   │   ├── dto/           # Objetos de transferência (request/response)
    │   │   ├── exception/     # Exceções de domínio + handler global
    │   │   ├── mapper/        # Conversão entidade ↔ DTO
    │   │   ├── model/         # Entidades JPA (Usuario, Participante, Aluno, Professor, Empresa...)
    │   │   ├── repository/    # Spring Data JPA repositories
    │   │   └── service/       # Regras de negócio
    │   └── README.md          # Documentação detalhada do back-end
    └── app/                   # Front-end — React + Vite
        ├── src/
        │   ├── components/    # Componentes reutilizáveis (AuthForm, VantagemCard, ProtectedRoute...)
        │   ├── context/       # AuthContext (estado global de autenticação)
        │   ├── pages/         # Telas por perfil (aluno, professor, empresa, login, registro)
        │   ├── services/      # Camada de integração com a API (Axios)
        │   └── utils/         # Funções utilitárias (máscaras de CPF/CNPJ/RG)
        └── README.md          # Documentação detalhada do front-end
```

---

## 👤 Histórias de Usuário

| Como | Quero | Para |
|-----|----------------|-----------------------------|
| Usuário          | Realizar login e cadastro na plataforma      | Participar do sistema de mérito estudantil            |
| Agente Parceiro| Cadastrar vantagens oferecidas pela minha empresa                         | Alunos trocarem moedas por benefícios                 |
| Professor      | Distribuir moedas aos alunos com mensagem de reconhecimento              | Estimular bom comportamento e participação            |
| Participante | Consultar extrato de moedas e transações                                | Acompanhar saldo e histórico de movimentações         |
| Aluno          | Trocar moedas por vantagens                                             | Usufruir dos benefícios do sistema                    |
| Aluno          | Ser notificado por email ao receber moedas ou resgatar vantagens        | Acompanhar conquistas e trocas                        |

---

## ✅ Requisitos

### Funcionais

| RF  | Requisito |
|------|---------------------------------------------------------------------------------------------------------|
| RF1  | Permitir cadastro de alunos, vinculando-os a instituições e cursos pré-cadastrados                      |
| RF2  | Permitir cadastro de empresas parceiras e das vantagens oferecidas                                      |
| RF3  | Permitir que professores distribuam moedas aos alunos, com mensagem obrigatória                         |
| RF4  | Controlar saldo de moedas dos professores, acumulando saldo não utilizado a cada semestre               |
| RF5  | Notificar alunos por email ao receber moedas                                                            |
| RF6  | Permitir consulta de extrato de moedas e transações por alunos e professores                            |
| RF7  | Permitir que alunos troquem moedas por vantagens cadastradas                                            |
| RF8  | Enviar emails de cupom para alunos e empresas ao resgatar uma vantagem, incluindo código de conferência |
| RF9  | Exigir autenticação (login e senha) para acesso de alunos, professores e empresas                       |
| RF10 | Garantir que apenas usuários autenticados possam realizar operações sensíveis                           |

### Não Funcionais

| RNF  | Requisito |
|------|---------------------------------------------------------------------------------------------------------|
| RNF1  | O sistema deve ser desenvolvido utilizando a arquitetura MVC |
| RNF2  | O sistema deve enviar notificações por email de forma automática |

---

## 📜 Regras de Negócio

| RN   | Regra |
|------|--------------------------------------------------------------------------------------------------------------|
| RN1  | Cada professor recebe 1.000 moedas por semestre, acumulando saldo não utilizado                              |
| RN2  | Professores só podem distribuir moedas se possuírem saldo suficiente                                         |
| RN3  | Toda distribuição de moedas deve ser acompanhada de uma mensagem de reconhecimento                           |
| RN4  | Alunos só podem trocar moedas se possuírem saldo suficiente                                                  |
| RN5  | Empresas parceiras devem cadastrar descrição e foto para cada vantagem oferecida                             |
| RN6  | Ao resgatar uma vantagem, o valor em moedas é descontado do saldo do aluno                                   |
| RN7  | Emails de cupom enviados ao aluno e à empresa devem conter um código único para conferência                  |
| RN8  | Instituições e professores são pré-cadastrados no sistema                                                    |
| RN9  | O sistema deve registrar todas as transações de envio, recebimento e troca de moedas para consulta posterior |
| RN10 | O acesso ao sistema é restrito a usuários autenticados                                                       |

---

## 🗺️ Modelagem

### Diagrama de Casos de Uso

![UseCaseDiagram](/projeto/DiagramaDeCasosDeUso.drawio.svg)

### Diagrama de Classe

![ClassDiagram](/projeto/DiagramaDeClasse.drawio.svg)

### Diagrama de Componentes

![ComponentDiagram](/projeto/DiagramaDeComponentes.drawio.png)

### Diagrama de Implantação

![ImplantationDiagram](/projeto/DiagramaDeImplantacao.jpg)

### Diagrama de Entidade-Relacionamento

![ERDiagram](/projeto/DiagramaER.drawio.png)

### Modelo de Entidade Relacional

![MRModel](/projeto/ModeloER.drawio.svg)

### Diagramas de Sequência

#### Diagrama de Sequência de Gestão de Vantagem

![SequenceDiagram_AdvantageManagement](/projeto/DiagramaDeSequencia_GestaoDeVantagem.svg)

#### Diagrama de Sequência de Visualizar Vantagens

![SequenceDiagram_GetAdvantages](/projeto/DiagramaDeSequencia_VisualizarVantagens.svg)

#### Diagrama de Sequência de Distribuição de Moedas

![SequenceDiagram_SendCoins](/projeto/DiagramaDeSequencia_DistribuicaoDeMoedas.svg)

#### Diagrama de Sequência de Consulta de Extrato

![SequenceDiagram_GetTransactions](/projeto/DiagramaDeSequencia_ConsultarExtrato.svg)

#### Diagrama de Sequência de Resgate de Vantagem

![SequenceDiagram_RedeemAdvantage](/projeto/DiagramaDeSequencia_ResgateDeVantagem.svg)

#### Diagrama de Sequência de Notificação

![SequenceDiagram_Notification](/projeto/DiagramaDeSequencia_Notificacao.svg)

### Diagramas de Comunicação

#### Diagrama de Comunicação de Gestão de Vantagem

![CommunicationDiagram_AdvantageManagement](/projeto/DiagramaDeComunicacao_GestaoDeVantagem.drawio.png)

#### Diagrama de Comunicação de Visualizar Vantagens

![CommunicationDiagram_GetAdvantages](/projeto/DiagramaDeComunicacao_VisualizarVantagens.drawio.svg)

#### Diagrama de Comunicação de Distribuição de Moedas

![CommunicationDiagram_SendCoins](/projeto/DiagramaDeComunicacao_DistribuicaoDeMoedas.drawio.svg)

#### Diagrama de Comunicação de Consulta de Extrato

![CommunicationDiagram_GetTransactions](/projeto/DiagramaDeComunicacao_ConsultarExtrato.drawio.svg)

#### Diagrama de Comunicação de Resgate de Vantagens

![CommunicationDiagram_RedeemAdvantage](/projeto/DiagramaDeComunicacao_ResgateDeVantagens.drawio.png)

#### Diagrama de Comunicação de Notificação

![CommunicationDiagram_Notification](/projeto/DiagramaDeComunicacao_Notificacao.drawio.svg)

---

## 🗄️ Definição e implementação da estratégia de acesso ao banco de dados

A estratégia de acesso a dados visa isolar a lógica de negócio dos detalhes de armazenamento do SGBD. A aplicação utiliza o **Spring Data JPA**, um framework ORM que implementa o padrão Repository, sobre um banco **MySQL**.

- Classes do modelo de domínio (pasta `model`) são mapeadas para o banco de dados, incluindo hierarquias de herança (`Usuario` → `Participante` → `Aluno`/`Professor`, `Transacao` → `Distribuicao`/`Resgate`).
- A pasta `repository` define o contrato de acesso aos dados via interfaces `JpaRepository`.
- A camada `service` (camada de negócio) utiliza os repositories (camada de dados) para executar a lógica de negócio e gerencia transações (`@Transactional`) para garantir a consistência dos dados — por exemplo, débito de saldo e registro da transação ocorrem de forma atômica.
- A camada `controller` consome os services e expõe a API REST, comunicando-se através de DTOs convertidos pelos `mapper`s, sem nunca expor as entidades JPA diretamente.
- Um `DataInitializer` popula o banco com instituições, professores e saldos iniciais na primeira execução, viabilizando testes e demonstrações.

Para mais detalhes da implementação, consulte [`repository/`](/implementacao/api/src/main/java/com/projeto/lab/implementacao/repository), [`model/`](/implementacao/api/src/main/java/com/projeto/lab/implementacao/model) e a [documentação completa do back-end](/implementacao/api/README.md#-estratégia-de-persistência).

---

## 🚀 Como executar

O projeto é dividido em dois módulos que rodam de forma independente. Cada um possui um README dedicado com pré-requisitos, variáveis de ambiente e passo a passo completos:

| Módulo | Stack | Documentação |
|---|---|---|
| **Back-end** (`implementacao/api`) | Java 21 · Spring Boot 3.5.6 · MySQL · JWT | [implementacao/api/README.md](/implementacao/api/README.md) |
| **Front-end** (`implementacao/app`) | React 19 · Vite · React Router · Axios | [implementacao/app/README.md](/implementacao/app/README.md) |

Resumo rápido para subir o ambiente local:

```bash
# 1. Clone o repositório
git clone https://github.com/marcosffp/Sistema-Merito-Digital.git
cd Sistema-Merito-Digital

# 2. Suba o back-end (consulte implementacao/api/README.md para o .env)
cd implementacao/api
./mvnw spring-boot:run

# 3. Em outro terminal, suba o front-end
cd implementacao/app
npm install
npm run dev
```

Após subir os dois módulos, a aplicação fica disponível em `http://localhost:5173` (front-end) consumindo a API em `http://localhost:8080` (back-end), com documentação interativa em `http://localhost:8080/swagger-ui.html`.

---

## 📦 Tecnologias e dependências

| Categoria | Tecnologia | Versão |
|---|---|---|
| Linguagem (back-end) | Java | 21 |
| Framework (back-end) | Spring Boot | 3.5.6 |
| Persistência | Spring Data JPA + Hibernate | — |
| Banco relacional | MySQL (driver `mysql-connector-j`) | 8 |
| Banco em memória (testes) | H2 | — |
| Segurança | Spring Security + JWT (`jjwt`, `java-jwt`) | 0.11.5 / 4.4.0 |
| Documentação de API | Springdoc OpenAPI (Swagger UI) | 2.8.9 |
| Upload de imagens | Cloudinary (`cloudinary-http44`) | 1.34.0 |
| E-mail | Spring Boot Starter Mail | — |
| Variáveis de ambiente | spring-dotenv | 4.0.0 |
| Boilerplate | Lombok | — |
| Build (back-end) | Maven | 3.9+ |
| Linguagem (front-end) | JavaScript (JSX) | — |
| Framework (front-end) | React | 19.1 |
| Bundler / dev server | Vite | 7.1 |
| Roteamento | React Router DOM | 7.9 |
| Cliente HTTP | Axios | 1.13 |
| Ícones | React Icons | 5.5 |
| Lint | ESLint | 9.36 |

---

<div align="center">
  <img width="70%" alt="pucminas" src="images/banner-institucional.svg"/>
</div>
<p align="center">Fonte do banner: <a href="https://github.com/joaopauloaramuni">João Paulo Carneiro Aramuni</a></p>
