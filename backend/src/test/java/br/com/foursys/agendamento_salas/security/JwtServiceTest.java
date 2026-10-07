package br.com.foursys.agendamento_salas.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String TEST_SECRET =
            "Zm91cnNjaGVkdWxlci10b2tlbi1kZS10ZXN0ZS1uYW8tdXNhci1lbS1wcm9kdWNhbw==";

    @Test
    void deveGerarTokenValidoComEmailDoUsuario() {
        JwtService service = serviceWithExpiration(60);
        UserDetails user = user("ana@example.com");

        String token = service.generateToken(user);

        assertEquals("ana@example.com", service.extractUsername(token));
        assertTrue(service.isTokenValid(token, user));
    }

    @Test
    void deveRecusarTokenQuandoEmailNaoCorrespondeAoUsuario() {
        JwtService service = serviceWithExpiration(60);
        String token = service.generateToken(user("ana@example.com"));

        assertFalse(service.isTokenValid(token, user("outra@example.com")));
    }

    private JwtService serviceWithExpiration(long seconds) {
        JwtService service = new JwtService(TEST_SECRET);
        ReflectionTestUtils.setField(service, "expirationSeconds", seconds);
        return service;
    }

    private UserDetails user(String email) {
        return User.withUsername(email).password("senha").roles("USER").build();
    }
}
