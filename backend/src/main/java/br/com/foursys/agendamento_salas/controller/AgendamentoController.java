package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.security.AuthenticatedUserProvider;
import br.com.foursys.agendamento_salas.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody CriarAgendamentoRequest agendamentoRequest,
            AuthenticatedUserProvider usuario
    ) {
        Long usuarioAutenticado = usuario.get().id();

        agendamentoService.create(agendamentoRequest, usuarioAutenticado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
