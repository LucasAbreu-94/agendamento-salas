# Padrão Git

Este documento define o fluxo de versionamento adotado neste projeto.

---

## Estratégia de Branches

O projeto utiliza a seguinte estrutura de branches:

```text
main
│
develop
│
├── feature/*
├── bugfix/*
├── refactor/*
├── docs/*
├── test/*
└── chore/*
```

### main

- Contém apenas versões estáveis.
- Não deve receber commits diretamente.
- Recebe alterações apenas através de Pull Request da branch `develop`.

### develop

- Branch principal de desenvolvimento.
- Todas as novas funcionalidades devem ser criadas a partir dela.

### feature/*

Utilizada para implementação de novas funcionalidades.

Exemplos:

```text
feature/agendamento-salas
feature/reserva-sala
feature/listagem-salas
```

### bugfix/*

Utilizada para correção de bugs.

Exemplos:

```text
bugfix/validacao-horario
bugfix/cors
```

### refactor/*

Utilizada para melhorias internas sem alteração de comportamento.

Exemplos:

```text
refactor/sala-service
refactor/reserva-controller
```

### docs/*

Utilizada para alterações de documentação.

Exemplos:

```text
docs/readme
docs/arquitetura
docs/padrao-git
```

### test/*

Utilizada para criação ou melhoria de testes.

Exemplos:

```text
test/reserva-service
test/sala-controller
```

### chore/*

Utilizada para tarefas de manutenção e configuração do projeto.

Exemplos:

```text
chore/github-actions
chore/editorconfig
chore/prettier
```

---

## Conventional Commits

Todos os commits devem seguir o padrão Conventional Commits.

| Tipo | Descrição |
| --- | --- |
| feat | Nova funcionalidade |
| fix | Correção de bug |
| docs | Alteração de documentação |
| refactor | Refatoração sem alteração de comportamento |
| style | Alterações de formatação |
| test | Inclusão ou alteração de testes |
| build | Alterações de build |
| ci | Configuração de Integração Contínua |
| perf | Melhoria de performance |
| chore | Manutenção do projeto |

### Exemplos

```text
feat: adiciona tela de agendamento

feat: implementa busca de salas disponíveis

fix: corrige validação de horário

docs: atualiza arquitetura

refactor: simplifica SalaService

test: adiciona testes para ReservaService

ci: adiciona workflow do backend

chore: atualiza dependências
```

---

## Fluxo de Desenvolvimento

Toda nova implementação deve seguir o fluxo abaixo.

```text
develop
    │
    ├── Criar branch
    │
    ├── Implementar funcionalidade
    │
    ├── Commits pequenos
    │
    ├── Push
    │
    ├── Pull Request
    │
    ├── GitHub Actions
    │
    └── Merge para develop
```

Quando uma versão estiver pronta:

```text
develop
    │
    ▼
main
    │
    ▼
Tag
    │
    ▼
Release
```

---

## Pull Requests

Toda Pull Request deve:

- Possuir um título claro e objetivo.
- Manter um único objetivo por PR.
- Conter uma breve descrição das alterações realizadas.
- Estar com os testes e builds aprovados.
- Não incluir alterações fora do escopo da funcionalidade.

---

## Boas Práticas

- Nunca realizar commits diretamente na `main`.
- Criar uma branch para cada funcionalidade.
- Utilizar nomes de branches claros e padronizados.
- Realizar commits pequenos e frequentes.
- Escrever mensagens de commit objetivas.
- Executar testes antes de realizar o Push.
- Manter a branch atualizada com `develop`.
- Evitar commits contendo código quebrado.
- Nunca versionar credenciais, tokens ou arquivos sensíveis.

---

## GitHub Actions

Todo Push ou Pull Request poderá executar automaticamente os workflows configurados no repositório.

As validações podem incluir:

- Build do Backend.
- Testes do Backend.
- Build do Frontend.
- Testes do Frontend.
- Outras verificações configuradas no projeto.

O merge deve ocorrer somente quando todas as validações forem concluídas com sucesso.

---

## Versionamento

O projeto seguirá Versionamento Semântico (Semantic Versioning).

Formato:

```text
MAJOR.MINOR.PATCH
```

Exemplos:

```text
v0.1.0
Primeiro MVP

v0.2.0
Fluxo de reserva implementado

v0.3.0
Integração com Microsoft Graph

v1.0.0
Primeira versão estável
```
