package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


}
