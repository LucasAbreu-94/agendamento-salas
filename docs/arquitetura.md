# Arquitetura

## Estado atual

O sistema é um MVP sem banco de dados, autenticação corporativa ou integrações externas. As salas e reservas são simuladas para apresentar o fluxo completo.

## Fluxo técnico

```text
Angular (localhost:4200) -> Spring Boot (localhost:8080) -> serviços mockados
```

No futuro, apenas o backend poderá integrar IA e Microsoft Graph. O frontend continuará consumindo endpoints próprios da API.

## Evolução prevista

Os contratos dos serviços devem permitir a troca gradual de dados mockados por Microsoft Graph, sem modificar o fluxo de telas ou regras de negócio do frontend.
