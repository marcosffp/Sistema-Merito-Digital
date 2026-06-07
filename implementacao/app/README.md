<img width="1600" style="height:auto; border-radius: 12px;" alt="banner" src="../../images/banner.png" />

# Frontend — Sistema de Mérito Digital

> Single Page Application em React responsável pela experiência de alunos, professores e empresas parceiras: autenticação, distribuição e consulta de moedas, gestão de vantagens e resgate com geração de cupons — tudo consumindo a API REST do back-end via Axios.

---

## 🛠️ Stack Principal

![React](https://img.shields.io/badge/React-19.1-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-7.1-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![React Router](https://img.shields.io/badge/React_Router-7.9-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-1.13-5A29E4?style=for-the-badge&logo=axios&logoColor=white)
![React Icons](https://img.shields.io/badge/React_Icons-5.5-E91E63?style=for-the-badge&logo=react&logoColor=white)
![ESLint](https://img.shields.io/badge/ESLint-9.36-4B32C3?style=for-the-badge&logo=eslint&logoColor=white)
![CSS Modules](https://img.shields.io/badge/CSS_Modules-Scoped_Styles-1572B6?style=for-the-badge&logo=css3&logoColor=white)

---

## 📑 Sumário

- [Sobre o front-end](#-sobre-o-front-end)
- [Arquitetura da aplicação](#-arquitetura-da-aplicação)
- [Estrutura de pastas](#-estrutura-de-pastas)
- [Autenticação e rotas protegidas](#-autenticação-e-rotas-protegidas)
- [Páginas e rotas](#-páginas-e-rotas)
- [Camada de serviços (API)](#-camada-de-serviços-api)
- [Identidade visual](#-identidade-visual)
- [Variáveis de configuração](#-variáveis-de-configuração)
- [Instalação e execução](#-instalação-e-execução)
- [Tecnologias e dependências](#-tecnologias-e-dependências)

---

## 📖 Sobre o front-end

Esta aplicação React (criada com **Vite**) é a interface do **Sistema de Mérito Digital**, oferecendo experiências dedicadas para os três perfis de usuário do ecossistema:

- **Alunos**: visualizam seu painel, consultam saldo e extrato de moedas, navegam pelo catálogo de vantagens, resgatam vantagens e acompanham seus resgates;
- **Professores**: visualizam seus alunos, distribuem moedas com mensagem de reconhecimento e consultam o extrato de distribuições realizadas;
- **Empresas parceiras**: gerenciam o catálogo de vantagens oferecidas (cadastro, edição e exclusão, com upload de imagem).

A navegação é protegida por **autenticação JWT** e **controle de acesso por papel (role)**, garantindo que cada perfil acesse somente as telas e ações que lhe correspondem.

---

## 🏛️ Arquitetura da aplicação

A aplicação segue o modelo padrão de SPAs React: **páginas** compõem **componentes reutilizáveis**, que dependem de um **contexto global de autenticação** e consomem a API através de uma **camada de serviços** baseada em Axios.

```
┌──────────────────────────────────────────────────────────────┐
│                          main.jsx                            │
│                  ReactDOM.createRoot · <App />               │
└────────────────────────────┬─────────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────────┐
│           App.jsx — AuthProvider + Router + Routes           │
│   Define rotas públicas, de cadastro e protegidas por role   │
└──────┬────────────────┬───────────────┬──────────────────────┘
       │                │               │
   AuthContext     ProtectedRoute    Pages (por perfil:
   (estado global   (guarda de        aluno / professor /
    de sessão,       rota baseada      empresa / login /
    user, token)     em role)          registro)
                                            │
                                  Components reutilizáveis
                                  (AuthForm, VantagemCard,
                                   Loading...)
                                            │
                                   services/ (Axios + api.js)
                                            │
                                  ─────── API REST (Spring Boot) ───────
```

**Decisões de implementação:**

| Decisão | Detalhe |
|---|---|
| Estado global de autenticação | `AuthContext` expõe `user`, `isAuthenticated`, `loading`, `login()` e `logout()` para toda a árvore de componentes |
| Guarda de rota por papel | `ProtectedRoute` redireciona para `/login` (sem sessão) ou `/unauthorized` (papel não permitido) com base em `allowedRoles` |
| Token persistido no cliente | O JWT é salvo em `localStorage` e injetado automaticamente em toda requisição via interceptor do Axios |
| Tratamento global de sessão expirada | Interceptor de resposta do Axios detecta `401`/`403`, limpa o token e redireciona para `/login` |
| Estilos isolados por componente/página | CSS Modules (`*.module.css`) evitam vazamento de estilos entre telas |
| Máscaras de formulário centralizadas | `utils/masks.js` formata CPF, CNPJ e RG de forma consistente em todos os formulários |

---

## 📁 Estrutura de pastas

```
app/
├── public/
├── src/
│   ├── main.jsx                       # Ponto de entrada — monta <App /> no DOM
│   ├── App.jsx                        # Definição de rotas (públicas, registro e protegidas por role)
│   ├── index.css                      # Estilos globais e variáveis de tema (cores, scrollbar)
│   ├── context/
│   │   └── AuthContext.jsx            # Provider com estado de sessão (user, login, logout, isAuthenticated)
│   ├── components/
│   │   ├── AuthForm.jsx               # Formulário compartilhado de login/cadastro
│   │   ├── ProtectedRoute.jsx         # Guarda de rota — exige autenticação e/ou papel específico
│   │   ├── VantagemCard.jsx           # Card de exibição de uma vantagem no catálogo
│   │   └── Loading.jsx                # Indicador de carregamento reutilizável
│   ├── pages/
│   │   ├── login/LoginPage.jsx
│   │   ├── registro/RegisterAlunoPage.jsx · RegisterEmpresaPage.jsx
│   │   ├── dashboard/DashboardAlunoPage.jsx · DashboardProfessorPage.jsx · DashboardEmpresaPage.jsx
│   │   ├── aluno/VantagensAlunoPage.jsx · ExtratoAlunoPage.jsx · ResgatesAlunoPage.jsx
│   │   ├── professor/VisualizarAlunosPage.jsx · DistribuirMoedasPage.jsx · ExtratoProfessorPage.jsx
│   │   ├── empresa/VantagensEmpresaPage.jsx · CadastrarVantagemPage.jsx · EditarVantagemPage.jsx
│   │   └── UnauthorizedPage.jsx        # Tela exibida quando o papel não tem permissão para a rota
│   ├── services/
│   │   ├── api.js                     # Instância Axios + interceptors (token JWT, tratamento de 401/403)
│   │   ├── authservice.js             # login, registro (aluno/empresa), sessão
│   │   ├── alunoservice.js            # dados, resumo e extrato do aluno
│   │   ├── professorservice.js        # dados do professor, lista de alunos, distribuição e extrato
│   │   ├── vantagemservice.js         # CRUD de vantagens, resgate e filtro por custo máximo
│   │   └── emailservice.js            # integrações relacionadas a notificações por e-mail
│   └── utils/
│       └── masks.js                   # Máscaras de CPF, CNPJ, RG e remoção de máscara
├── index.html
├── vite.config.js
├── eslint.config.js
└── package.json
```

---

## 🔐 Autenticação e rotas protegidas

O fluxo de sessão é centralizado no `AuthContext` e reforçado em duas camadas — interface (`ProtectedRoute`) e transporte (interceptors do Axios):

```
1. Usuário envia e-mail/senha em /login
       │
       ▼
2. authService.login() → POST /auth/login → recebe { token }
       │  token decodificado e persistido em localStorage
       ▼
3. AuthContext atualiza { user, isAuthenticated: true }
       │
       ▼
4. ProtectedRoute libera o acesso conforme allowedRoles
       │  ex.: ['Aluno'] · ['Professor'] · ['Empresa']
       ▼
5. Toda requisição subsequente carrega "Authorization: Bearer <token>"
   (injetado pelo interceptor de api.js)
       │
       ▼
6. Resposta 401/403 → token removido + redirecionamento para /login
   (interceptor de resposta de api.js)
```

- **Sem sessão válida** → qualquer rota protegida redireciona para `/login`.
- **Sessão válida, mas papel incompatível** com `allowedRoles` da rota → redirecionamento para `/unauthorized`.
- O **papel (`role`)** vem embutido no token JWT emitido pela API (`Aluno`, `Professor` ou `Empresa`) e determina quais menus, dashboards e ações ficam visíveis.

---

## 🗺️ Páginas e rotas

| Rota | Página | Acesso |
|---|---|---|
| `/login` | `LoginPage` | Pública |
| `/cadastro/aluno` | `RegisterAlunoPage` | Pública |
| `/cadastro/empresa` | `RegisterEmpresaPage` | Pública |
| `/unauthorized` | `UnauthorizedPage` | Pública |
| `/` | Redireciona para `/login` | Pública |
| `/dashboard/aluno` | `DashboardAlunoPage` | 🔒 Aluno |
| `/aluno/vantagens` | `VantagensAlunoPage` — catálogo de vantagens disponíveis para resgate | 🔒 Aluno |
| `/aluno/extrato` | `ExtratoAlunoPage` — extrato de moedas recebidas e gastas | 🔒 Aluno |
| `/aluno/resgates` | `ResgatesAlunoPage` — histórico de vantagens resgatadas e cupons | 🔒 Aluno |
| `/dashboard/professor` | `DashboardProfessorPage` | 🔒 Professor |
| `/professor/alunos` | `VisualizarAlunosPage` — lista de alunos da instituição | 🔒 Professor |
| `/professor/distribuir` | `DistribuirMoedasPage` — formulário de distribuição de moedas com mensagem de reconhecimento | 🔒 Professor |
| `/professor/extrato` | `ExtratoProfessorPage` — extrato de moedas distribuídas e saldo disponível | 🔒 Professor |
| `/dashboard/empresa` | `DashboardEmpresaPage` | 🔒 Empresa |
| `/empresa/vantagens` | `VantagensEmpresaPage` — gestão do catálogo de vantagens da empresa | 🔒 Empresa |
| `/empresa/vantagens/nova` | `CadastrarVantagemPage` — cadastro de vantagem (com upload de imagem) | 🔒 Empresa |
| `/empresa/vantagens/editar/:id` | `EditarVantagemPage` — edição de vantagem existente | 🔒 Empresa |

---

## 🔌 Camada de serviços (API)

Toda comunicação com o back-end passa por `services/`, que encapsula chamadas Axios em funções nomeadas por domínio — mantendo as páginas livres de detalhes de transporte HTTP:

| Serviço | Principais funções | Endpoints consumidos |
|---|---|---|
| `api.js` | Instância Axios base (`baseURL`, headers), interceptor de requisição (injeta `Authorization: Bearer <token>`) e interceptor de resposta (trata `401`/`403`, limpa sessão e redireciona) | — |
| `authservice.js` | `login(email, senha)`, `register(data, type)`, gestão de sessão local | `POST /auth/login`, `POST /alunos/cadastro`, `POST /empresas/cadastro` |
| `alunoservice.js` | `obterDadosAluno`, `obterResumoAluno`, `obterExtratoAluno` | `GET /alunos/{id}/completo`, `/resumo`, `GET /participantes/{id}/extrato` |
| `professorservice.js` | `obterDadosProfessor`, `listarAlunos`, `distribuirMoedas`, `obterExtratoProfessor` | `GET /professores/{id}`, `GET /alunos/resumo`, `POST /distribuicoes`, `GET /participantes/{id}/extrato` |
| `vantagemservice.js` | `listarVantagens`, `buscarVantagemPorId`, `cadastrarVantagem`, `atualizarVantagem`, `deletarVantagem`, `resgatarVantagem`, `listarPorCustoMaximo` | `GET/POST/PUT/DELETE /vantagens`, `GET /vantagens/custo-maximo`, `POST /resgates` |
| `emailservice.js` | Funções auxiliares para fluxos de notificação por e-mail (cupom de resgate, confirmação de recebimento de moedas) | — |

A `api.js` define `baseURL: 'http://localhost:8080'` — ajuste esse valor caso o back-end esteja rodando em outro host/porta (ver [Variáveis de configuração](#-variáveis-de-configuração)).

---

## 🎨 Identidade visual

As variáveis de tema globais ficam centralizadas em `src/index.css`:

| Variável | Cor | Uso |
|---|---|---|
| `--branco` | `#ffffff` | Fundo padrão |
| `--preto` | `#161616` | Texto principal |
| `--cinza` | `#e7e7e7` | Fundos secundários, trilha da scrollbar |
| `--cinza-escuro` | `#666666` | Texto secundário, thumb da scrollbar |
| `--azul-primario` | `#667eea` | Cor primária — botões, links e destaques |
| `--roxo-primario` | `#764ba2` | Cor de apoio — gradientes e elementos de destaque |

Cada página e componente possui seu próprio arquivo `*.module.css`, garantindo estilos com escopo local e evitando conflitos entre telas de perfis diferentes.

---

## ⚙️ Variáveis de configuração

A aplicação não depende de variáveis de ambiente do Vite por padrão — a URL da API é definida diretamente em [`src/services/api.js`](/implementacao/app/src/services/api.js):

```js
const api = axios.create({
  baseURL: 'http://localhost:8080', // ajuste para a URL do back-end em outros ambientes
  headers: { 'Content-Type': 'application/json' },
});
```

> Para apontar a aplicação para um back-end remoto (homologação/produção), atualize esse valor — ou, se preferir, extraia-o para uma variável de ambiente do Vite (`import.meta.env.VITE_API_URL`) e crie um arquivo `.env` na raiz de `implementacao/app/`.

---

## 🚀 Instalação e execução

### Pré-requisitos

- Node.js 18+ e npm
- Back-end da aplicação em execução em `http://localhost:8080` (veja [implementacao/api/README.md](/implementacao/api/README.md))

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/marcosffp/Sistema-Merito-Digital.git
cd Sistema-Merito-Digital/implementacao/app

# 2. Instale as dependências
npm install

# 3. Suba o servidor de desenvolvimento (Vite + HMR)
npm run dev
```

A aplicação fica disponível em `http://localhost:5173`.

### Scripts disponíveis

```bash
npm run dev       # Sobe o servidor de desenvolvimento com Hot Module Replacement
npm run build     # Gera o build de produção (pasta dist/)
npm run preview   # Serve o build de produção localmente para verificação
npm run lint      # Executa o ESLint sobre o projeto
```

---

## 📦 Tecnologias e dependências

| Categoria | Tecnologia | Versão |
|---|---|---|
| Biblioteca de UI | React | 19.1 |
| Bundler / dev server | Vite (com plugin SWC para Fast Refresh) | 7.1 |
| Roteamento | React Router DOM | 7.9 |
| Cliente HTTP | Axios | 1.13 |
| Ícones | React Icons | 5.5 |
| Estilos | CSS Modules | — |
| Lint | ESLint + plugins (`react-hooks`, `react-refresh`) | 9.36 |
| Tipos (dev) | `@types/react`, `@types/react-dom` | 19.1 |
| Gerenciador de pacotes | npm | — |

---

<div align="center">
  <img width="70%" alt="pucminas" src="../../images/banner-institucional.svg"/>
</div>
<p align="center">Fonte do banner: <a href="https://github.com/joaopauloaramuni">João Paulo Carneiro Aramuni</a></p>
