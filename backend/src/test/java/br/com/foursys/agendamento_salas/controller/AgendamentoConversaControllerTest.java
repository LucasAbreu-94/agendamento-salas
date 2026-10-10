
package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.EnviarMensagemRequest;
import br.com.foursys.agendamento_salas.dto.request.SelecionarSalaRequest;
import br.com.foursys.agendamento_salas.dto.response.IniciarConversaResponse;
import br.com.foursys.agendamento_salas.dto.response.OrquestradorAgendamentoResponse;
import br.com.foursys.agendamento_salas.service.OrquestradorAgendamentoService;
import br.com.foursys.agendamento_salas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoConversaControllerTest {

    @Mock
    private OrquestradorAgendamentoService orquestradorAgendamentoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AgendamentoConversaController controller;

    @Test
    void deveIniciarConversaComSucesso() {
        Usuario usuario = mock(Usuario.class);
        IniciarConversaResponse response = mock(IniciarConversaResponse.class);

        when(authentication.getName()).thenReturn("usuario@teste.com");
        when(usuarioService.buscarPorEmail("usuario@teste.com"))
                .thenReturn(usuario);
        when(orquestradorAgendamentoService.iniciarConversa(usuario))
                .thenReturn(response);

        ResponseEntity<IniciarConversaResponse> resultado =
                controller.iniciarConversa(authentication);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertSame(response, resultado.getBody());

        verify(usuarioService).buscarPorEmail("usuario@teste.com");
        verify(orquestradorAgendamentoService).iniciarConversa(usuario);
    }

    @Test
    void deveProcessarMensagemComSucesso() {
        EnviarMensagemRequest request = mock(EnviarMensagemRequest.class);
        OrquestradorAgendamentoResponse response =
                mock(OrquestradorAgendamentoResponse.class);

        when(request.conversaId()).thenReturn(1L);
        when(request.mensagem()).thenReturn(
                "Quero reservar uma sala amanhã às 14h"
        );

        when(orquestradorAgendamentoService.processarMensagem(
                1L,
                "Quero reservar uma sala amanhã às 14h"
        )).thenReturn(response);

        ResponseEntity<OrquestradorAgendamentoResponse> resultado =
                controller.processarMensagem(request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertSame(response, resultado.getBody());

        verify(orquestradorAgendamentoService).processarMensagem(
                1L,
                "Quero reservar uma sala amanhã às 14h"
        );
    }

    @Test
    void deveSelecionarSalaComSucesso() {
        Long conversaId = 1L;
        SelecionarSalaRequest request = mock(SelecionarSalaRequest.class);
        OrquestradorAgendamentoResponse response =
                mock(OrquestradorAgendamentoResponse.class);

        when(orquestradorAgendamentoService.selecionarSala(
                conversaId,
                request
        )).thenReturn(response);

        ResponseEntity<OrquestradorAgendamentoResponse> resultado =
                controller.selecionarSala(conversaId, request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertSame(response, resultado.getBody());

        verify(orquestradorAgendamentoService)
                .selecionarSala(conversaId, request);
    }

    @Test
    void deveConfirmarAgendamentoComSucesso() {
        Long conversaId = 1L;
        OrquestradorAgendamentoResponse response =
                mock(OrquestradorAgendamentoResponse.class);

        when(orquestradorAgendamentoService.confirmarAgendamento(conversaId))
                .thenReturn(response);

        ResponseEntity<OrquestradorAgendamentoResponse> resultado =
                controller.confirmarAgendamento(conversaId);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertSame(response, resultado.getBody());

        verify(orquestradorAgendamentoService)
                .confirmarAgendamento(conversaId);
    }
}
