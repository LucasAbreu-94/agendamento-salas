package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.dto.response.ConfimacaoAgendamentoResponse;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.security.AuthenticatedUser;
import br.com.foursys.agendamento_salas.security.SecurityUtils;
import br.com.foursys.agendamento_salas.service.AgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AgendamentoControllerTests {

    private AgendamentoService agendamentoService;
    private SecurityUtils securityUtils;
    private AgendamentoController agendamentoController;

    @BeforeEach
    void setUp() {
        agendamentoService = mock(AgendamentoService.class);
        securityUtils = mock(SecurityUtils.class);
        agendamentoController = new AgendamentoController(agendamentoService, securityUtils);
    }

    @Test
    void deveCriarAgendamentoRetornandoStatus201EConfirmacaoDoNovoFluxo() {
        when(securityUtils.get()).thenReturn(new AuthenticatedUser(42L, "usuario@email.com"));

        Sala sala = Sala.builder()
                .id(12L)
                .nome("Sala Focus")
                .capacidade(8)
                .disponivel(true)
                .build();

        CriarAgendamentoRequest request = new CriarAgendamentoRequest(
                sala,
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                4,
                null,
                "Reunião de alinhamento"
        );

        ConfimacaoAgendamentoResponse expected = ConfimacaoAgendamentoResponse.builder()
                .id(99L)
                .sala(sala)
                .inicio(LocalTime.of(9, 0))
                .fim(LocalTime.of(10, 0))
                .status(StatusAgendamento.CONFIRMADO)
                .solicitante(null)
                .build();

        when(agendamentoService.create(request, 42L)).thenReturn(expected);

        var response = agendamentoController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(agendamentoService).create(request, 42L);
    }
}
