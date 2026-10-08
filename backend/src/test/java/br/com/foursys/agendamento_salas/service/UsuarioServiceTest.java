package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.CriarUsuarioRequest;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveCriarUsuarioArmazenandoSenhaCodificada() {
        Usuario usuarioSalvo = new Usuario();
        when(repository.buscarUsuarioEmail("maria@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha-segura")).thenReturn("senha-codificada");
        when(repository.salvar(org.mockito.ArgumentMatchers.any(Usuario.class))).thenReturn(usuarioSalvo);
        UsuarioService service = new UsuarioService(repository, passwordEncoder);

        Usuario resultado = service.criar(new CriarUsuarioRequest(
                "maria@example.com", "Maria", "senha-segura", PerfilUsuario.USER
        ));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).salvar(captor.capture());
        assertSame(usuarioSalvo, resultado);
        assertEquals("maria@example.com", captor.getValue().getEmail());
        assertEquals("Maria", captor.getValue().getNome());
        assertEquals("senha-codificada", captor.getValue().getPassword());
        assertEquals(PerfilUsuario.USER, captor.getValue().getPerfilUsuario());
        verify(passwordEncoder).encode("senha-segura");
    }

    @Test
    void deveRejeitarEmailJaCadastradoAntesDeCodificarSenha() {
        when(repository.buscarUsuarioEmail("maria@example.com"))
                .thenReturn(Optional.of(new Usuario()));
        UsuarioService service = new UsuarioService(repository, passwordEncoder);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.criar(new CriarUsuarioRequest(
                        "maria@example.com", "Maria", "senha-segura", PerfilUsuario.USER
                ))
        );

        assertEquals("Username já cadastrado", exception.getMessage());
        verifyNoInteractions(passwordEncoder);
        verify(repository).buscarUsuarioEmail("maria@example.com");
    }
}
