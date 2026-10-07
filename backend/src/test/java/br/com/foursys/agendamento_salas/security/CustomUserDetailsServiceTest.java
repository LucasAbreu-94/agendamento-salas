package br.com.foursys.agendamento_salas.security;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    @Test
    void deveCarregarPrincipalComDadosDoUsuarioEncontrado() {
        UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
        Usuario usuario = new Usuario();
        usuario.setId(14L);
        usuario.setEmail("ana@example.com");
        usuario.setPerfilUsuario(PerfilUsuario.USER);
        when(repository.buscarUsuarioEmail("ana@example.com")).thenReturn(Optional.of(usuario));
        CustomUserDetailsService service = new CustomUserDetailsService(repository);

        CustomUserDetails details = assertInstanceOf(
                CustomUserDetails.class,
                service.loadUserByUsername("ana@example.com")
        );

        assertEquals(14L, details.getId());
        assertEquals("ana@example.com", details.getUsername());
        verify(repository).buscarUsuarioEmail("ana@example.com");
    }

    @Test
    void deveRejeitarEmailNaoEncontrado() {
        UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
        when(repository.buscarUsuarioEmail("ausente@example.com")).thenReturn(Optional.empty());
        CustomUserDetailsService service = new CustomUserDetailsService(repository);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("ausente@example.com")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
