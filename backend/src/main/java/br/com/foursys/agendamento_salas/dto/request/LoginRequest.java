package br.com.foursys.agendamento_salas.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
