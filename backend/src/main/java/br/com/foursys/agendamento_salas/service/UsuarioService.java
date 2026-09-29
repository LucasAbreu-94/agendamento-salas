package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.dto.CriarUsuarioRequest;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criar (CriarUsuarioRequest request){
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username já cadastrado");
        }

        Usuario usuario = new Usuario();

        usuario.setUsername(request.username());

        usuario.setPassword(
                passwordEncoder.encode(request.password())
        );

        return usuarioRepository.save(usuario);

    }
}
