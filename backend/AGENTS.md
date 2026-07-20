# Backend — Instruções específicas

## Stack

- Java 21, Spring Boot 4.1 e Maven
- Spring Web MVC e Bean Validation
- JUnit 5 e Mockito
- Lombok somente quando trouxer benefício claro

## Organização

O código Java deve permanecer sob `src/main/java`. Organize preferencialmente por responsabilidade: `controller`, `service`, `dto`, `domain`, `repository`, `integration`, `config` e `exception`.

Enquanto o MVP for mockado, dados podem ficar em `mock/` ou em providers/services próprios. Mantenha contratos que permitam substituir o mock no futuro, por exemplo `SalaProviderMock` por `MicrosoftGraphSalaProvider`, sem implementar a integração real agora.

## Padrões

- Controllers tratam somente HTTP; regras de negócio ficam em services.
- Use DTOs ou `record`s nas APIs; não exponha entidades diretamente.
- Valide entradas com Bean Validation e use constructor injection.
- Não use `@Autowired` em atributos, não capture `Exception` genericamente e não crie métodos grandes.
- Retorne códigos HTTP coerentes e centralize erros com `@RestControllerAdvice` quando houver tratamento de erro.

## Regras a testar quando implementadas

- data passada e horários inválidos;
- término anterior ao início;
- capacidade e disponibilidade;
- sala não encontrada ou indisponível;
- confirmação de reserva.
