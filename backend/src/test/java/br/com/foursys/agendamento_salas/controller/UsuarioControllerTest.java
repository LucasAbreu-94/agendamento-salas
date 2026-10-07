package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.CriarUsuarioRequest;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {

    @Test
    void deveCriarUsuarioERetornarCreatedComUsuarioCriado() {
        UsuarioService service = mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController(service);
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "ana@example.com", "Ana", "senha-hash", PerfilUsuario.USER
        );
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setEmail("ana@example.com");
        when(service.criar(request)).thenReturn(usuario);

        var response = controller.criar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(usuario, response.getBody());
    }
}
