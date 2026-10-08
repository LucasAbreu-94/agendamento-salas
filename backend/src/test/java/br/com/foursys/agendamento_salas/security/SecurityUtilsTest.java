package br.com.foursys.agendamento_salas.security;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityUtilsTest {

    private final SecurityUtils securityUtils = new SecurityUtils();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveDevolverIdEEmailDoPrincipalCustomizadoAutenticado() {
        Usuario usuario = new Usuario();
        usuario.setId(23L);
        usuario.setEmail("ana@example.com");
        usuario.setPerfilUsuario(PerfilUsuario.USER);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(new CustomUserDetails(usuario), null, "ROLE_USER")
        );

        AuthenticatedUser result = securityUtils.get();

        assertEquals(new AuthenticatedUser(23L, "ana@example.com"), result);
    }

    @Test
    void deveFalharQuandoNaoHaAutenticacao() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, securityUtils::get);
    }

    @Test
    void deveFalharQuandoAutenticacaoNaoEstaValidada() {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("ana@example.com", null)
        );

        assertThrows(AuthenticationCredentialsNotFoundException.class, securityUtils::get);
    }

    @Test
    void deveFalharQuandoPrincipalNaoECustomUserDetails() {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("ana@example.com", null, "ROLE_USER")
        );

        assertThrows(AuthenticationCredentialsNotFoundException.class, securityUtils::get);
    }
}
