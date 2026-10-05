package br.com.foursys.agendamento_salas.dto;

import br.com.foursys.agendamento_salas.enums.PerfilUsuario;

public record CriarUsuarioRequest(
        String username,
        String password,
        PerfilUsuario perfilUsuario
) {
}
