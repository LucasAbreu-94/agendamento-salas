# Agendamento de Salas — Instruções do projeto

## Visão geral

Este repositório contém o MVP de um sistema corporativo de agendamento de salas. A primeira versão será demonstrável, com dados mockados, para validação com o gestor.

Integrações com Microsoft Graph, Outlook, Microsoft Entra ID e provedores de IA (Gemini, OpenAI ou Azure OpenAI) são futuras. Não implemente nenhuma delas sem solicitação explícita.

## Estrutura e tecnologias

- `backend/`: API REST com Java 21, Spring Boot 4.1 e Maven.
- `frontend/`: aplicação Angular 21 com TypeScript, CSS e componentes standalone.
- `docs/`: arquitetura, regras de negócio e fluxo do MVP.

Durante o desenvolvimento, Angular roda em `http://localhost:4200` e Spring Boot em `http://localhost:8080`. O frontend deve chamar somente a API do backend; credenciais e chaves, quando existirem, pertencem exclusivamente ao backend.

Leia os documentos relevantes em `docs/` antes de implementar uma tarefa.

## Variáveis de ambiente

O backend lê banco e autenticação de um arquivo `backend/.env`, que não é versionado. Consulte `backend/.env.example` para a lista de variáveis. Nunca versione valores reais.

## Regras gerais

- Banco de dados MySQL e Spring Security estão autorizados para usuários e autenticação; Microsoft Graph e APIs de IA continuam proibidos sem pedido explícito.
- Não alterar tecnologias, versões ou adicionar dependências sem necessidade clara.
- Não expor ou versionar senhas, tokens, chaves ou outras credenciais.
- Manter alterações pequenas, focadas e compatíveis com o comportamento existente.
- Priorizar legibilidade, nomes claros e responsabilidades bem separadas; evitar duplicação e abstrações prematuras.
- Execute os testes ou builds relacionados à alteração e informe o resultado.

## Fluxo do MVP

1. Usuário acessa o Portal.
2. Seleciona **Agendamento de Salas**.
3. Informa data, horário, duração e quantidade de pessoas.
4. O sistema apresenta salas mockadas disponíveis.
5. O usuário escolhe uma sala, revisa os dados e confirma a reserva.
6. O sistema exibe uma confirmação visual. Nesta etapa, nenhuma reserva é criada no Outlook.

## Salas mockadas

- Sala Focus — Tamboré
- Sala Comp 01 — Tamboré
- Sala Comp 02 — Tamboré
- Sala Comp 03 — Tamboré
- Sala Comp 04 — Tamboré
- Sala 01 — Tamboré
- Sala 02 — Tamboré
- Sala 03 — Tamboré

## Identidade visual

- Barra superior azul-marinho, símbolo laranja à esquerda e texto `Portal`.
- Não exibir o texto `CCH`.
- Fundo branco; ações principais e detalhes em laranja.
- Interface corporativa, limpa, minimalista e responsiva.
- O acesso deve se chamar **Agendamento de Salas**, usar ícone de calendário e pode exibir a etiqueta `Novo`.

## Comandos

No Windows:

```powershell
cd backend
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run

cd ..\frontend
npm.cmd start
npm.cmd test
npm.cmd run build
```
