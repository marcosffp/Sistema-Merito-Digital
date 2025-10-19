# Sistema de Mérito- Digital

## Histórias de Usuário

|Como| Quero| Para |
|-----|----------------|-----------------------------|
| Usuário          | Realizar login e cadastro na plataforma      | Participar do sistema de mérito estudantil            |
| Agente Parceiro| Cadastrar vantagens oferecidas pela minha empresa                         | Alunos trocarem moedas por benefícios                 |
| Professor      | Distribuir moedas aos alunos com mensagem de reconhecimento              | Estimular bom comportamento e participação            |
| Participante | Consultar extrato de moedas e transações                                | Acompanhar saldo e histórico de movimentações         |
| Aluno          | Trocar moedas por vantagens                                             | Usufruir dos benefícios do sistema                    |
| Aluno          | Ser notificado por email ao receber moedas ou resgatar vantagens        | Acompanhar conquistas e trocas                        |

---

## Requisitos

### Funcionais

| RF  | Requisito                                                                                               |
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

---

### Não Funcionais

| RNF  | Requisito                                                                                               |
|------|---------------------------------------------------------------------------------------------------------|
| RNF1  | O sistema deve ser desenvolvido utilizando a arquitetura MVC |
| RNF2  | O sistema deve enviar notificações por email de forma automática |

---

## Regras de Negócio

| RN   | Regra                                                                                                        |
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


## Modelagem

### Diagrama de Casos de Uso

![UseCaseDiagram](/projeto/DiagramaDeCasosDeUso.drawio.svg)

### Diagrama de Classe

![ClassDiagram](/projeto/DiagramaDeClasse.drawio.svg)

### Diagrama de Implantação

![ClassDiagram](/projeto/DiagramaDeImplantacao.jpg)

## Definição e implementação da estratégia de acesso ao banco de dados

A estratégia de acesso a dados visa isolar a lógica de negócio dos detalhes de armazenamento do SGBD. A aplicação utilizará o Spring Data JPA, um Framework ORM , que implementa o Padrão Repository.

- Classes do Modelo de Domínio (pasta model) são mapeadas para o banco de dados.
- Pasta repository define o contrato de acesso aos dados
- Camada service (Camada de Negócio) utiliza os repositories (Camada de Dados) para executar a lógica de negócio e gerencia Transações para garantir a consistência dos dados.
- Camada controller consome os services.