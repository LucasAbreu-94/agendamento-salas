# Regras de negócio

## Busca de salas

A busca recebe data, horário inicial, horário final (ou duração) e quantidade de pessoas.

## Validações

- A data não pode estar no passado.
- O horário final deve ser posterior ao horário inicial.
- A capacidade da sala deve ser igual ou superior à quantidade de pessoas.
- Apenas salas disponíveis podem ser exibidas e selecionadas.
- O usuário deve confirmar os dados antes de concluir a reserva.

## Estado do MVP

Os dados são mockados. Nenhuma reserva é registrada no Outlook e não há chamadas ao Microsoft Graph ou a serviços externos de IA.
