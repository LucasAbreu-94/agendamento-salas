package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
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
    private AgendamentoController agendamentoController;

    @BeforeEach
    void setUp() {
        agendamentoService = mock(AgendamentoService.class);
        agendamentoController = new AgendamentoController(agendamentoService);
    }

    @Test
    void deveCriarAgendamentoRetornandoStatus201() {
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        when(securityUtils.get()).thenReturn(new AuthenticatedUser(42L, "usuario@email.com"));

        CriarAgendamentoRequest request = new CriarAgendamentoRequest(
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                4,
                null,
                "Reunião de alinhamento"
        );

        var response = agendamentoController.create(request, securityUtils);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(agendamentoService).create(request, 42L);
    }
}
