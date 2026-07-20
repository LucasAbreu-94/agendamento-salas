# Frontend — Instruções específicas

## Stack e organização

- Angular 21, TypeScript, CSS, Angular Router e HttpClient.
- Use componentes standalone e Signals quando forem úteis.
- Organize `src/app` em `core/`, `shared/` e `features/` (como `portal`, `agendamento`, `salas` e `reservas`).

## Padrões

- Componentes pequenos, tipados e sem lógica de negócio relevante no template.
- Use serviços para chamadas HTTP; não use `any` sem justificativa.
- Modele interfaces reutilizáveis, trate carregamento, sucesso, vazio e erro.
- Garanta responsividade básica e acessibilidade em botões e campos.
- O Angular chama apenas endpoints do Spring Boot. Nunca chama diretamente Gemini, OpenAI, Azure OpenAI ou Microsoft Graph.

## Visual e fluxo

Siga a identidade visual definida no `AGENTS.md` da raiz: barra azul-marinho, símbolo e ações em laranja, fundo branco e aparência corporativa minimalista. As telas do MVP são Portal, entrada de dados/conversa, lista de salas, confirmação e reserva realizada.
