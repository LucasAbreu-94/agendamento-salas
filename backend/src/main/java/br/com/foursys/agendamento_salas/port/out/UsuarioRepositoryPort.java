package br.com.foursys.agendamento_salas.port.out;

import br.com.foursys.agendamento_salas.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario salvar (Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarUsuarioEmail(String username);
}
