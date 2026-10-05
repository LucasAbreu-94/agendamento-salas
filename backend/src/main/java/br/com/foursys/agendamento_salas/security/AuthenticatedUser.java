package br.com.foursys.agendamento_salas.security;

public record AuthenticatedUser(
        Long id,
        String email
) {
}
