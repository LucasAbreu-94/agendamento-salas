# Arquitetura

## Estado atual

O sistema é um MVP sem integrações externas. As salas e reservas são simuladas para apresentar o fluxo completo. A busca de salas já possui endpoint mockado (`GET /api/salas/disponiveis`).

Usuários e autenticação usam MySQL local com Spring Security e JWT. Os testes do backend rodam com H2 em memória (`backend/src/test/resources/application-test.yaml`), sem exigir um banco de verdade.

## Fluxo técnico

```text
Angular (localhost:4200) -> Spring Boot (localhost:8080) -> serviços mockados
                                                |
                                                +-> MySQL (usuários)
```

No futuro, apenas o backend poderá integrar IA e Microsoft Graph. O frontend continuará consumindo endpoints próprios da API.

## Estrutura do backend

`backend/src/main/java/br/com/foursys/agendamento_salas` é organizado por responsabilidade:

| Pacote | Responsabilidade |
|---|---|
| `controller` | HTTP, sem regra de negócio |
| `service` | regras de negócio |
| `domain` | entidades e registros do domínio |
| `repository` | acesso a dados (Spring Data JPA) |
| `dto` | payloads de entrada e saída das APIs |
| `exception` | exceções de negócio e `GlobalExceptionHandler` |
| `integration` | contratos de integração (calendário e IA), nas tasks futuras |
| `config`, `security` | configuração e segurança |

Erros seguem o modelo padrão de `docs/contratos-api.md`.

## Profiles

| Profile | Uso |
|---|---|
| `dev` | padrão em `spring-boot:run`; SQL e logs de debug |
| `prod` | produção; logs reduzidos |
| `test` | testes automatizados; H2 em memória |

Os testes ativam o profile com `@ActiveProfiles("test")`. Novas classes de teste precisam do mesmo annotation.

## Estrutura do frontend

`frontend/src/app` segue `core/`, `shared/` e `features/` (ver `frontend/AGENTS.md`). A base atual cria a feature `features/agendamento` com rota padrão. Os ambientes ficam em `frontend/src/environments`, com troca via `fileReplacements` na build de produção.

## Evolução prevista

Os contratos dos serviços, definidos em `docs/contratos-api.md`, devem permitir a troca gradual de dados mockados por Microsoft Graph, sem modificar o fluxo de telas ou regras de negócio do frontend.
