# Arquitetura

## Estado atual

O sistema é um MVP sem integrações externas. As salas e reservas são simuladas para apresentar o fluxo completo.

Usuários e autenticação usam MySQL local com Spring Security e JWT. Os testes do backend rodam com H2 em memória (`backend/src/test/resources/application.yaml`), sem exigir um banco de verdade.

## Fluxo técnico

```text
Angular (localhost:4200) -> Spring Boot (localhost:8080) -> serviços mockados
                                                |
                                                +-> MySQL (usuários)
```

No futuro, apenas o backend poderá integrar IA e Microsoft Graph. O frontend continuará consumindo endpoints próprios da API.

## Evolução prevista

Os contratos dos serviços devem permitir a troca gradual de dados mockados por Microsoft Graph, sem modificar o fluxo de telas ou regras de negócio do frontend.
