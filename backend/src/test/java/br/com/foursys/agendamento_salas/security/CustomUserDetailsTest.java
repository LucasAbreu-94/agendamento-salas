package br.com.foursys.agendamento_salas.security;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsTest {

    @Test
    void deveExporDadosDoUsuarioEPermissoesConfiguradas() {
        Usuario usuario = new Usuario();
        usuario.setId(5L);
        usuario.setEmail("admin@example.com");
        usuario.setPassword("senha-hash");
        usuario.setPerfilUsuario(PerfilUsuario.ADMIN);

        CustomUserDetails details = new CustomUserDetails(usuario);

        assertEquals(5L, details.getId());
        assertEquals("admin@example.com", details.getUsername());
        assertEquals("senha-hash", details.getPassword());
        assertEquals("ROLE_ADMIN", details.getAuthorities().iterator().next().getAuthority());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertFalse(details.getAuthorities().isEmpty());
    }
}
