package br.com.foursys.agendamento_salas.dto.request;

import br.com.foursys.agendamento_salas.enums.PerfilUsuario;

public record CriarUsuarioRequest(
        String email,
        String nome,
        String password,
        PerfilUsuario perfilUsuario
) {
}
