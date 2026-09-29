package br.com.foursys.agendamento_salas.dto;

public record CriarUsuarioRequest(
        String username,
        String password
) {
}
