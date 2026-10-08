package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.dto.response.AgendamentoResponse;
import br.com.foursys.agendamento_salas.dto.response.ConfimacaoAgendamentoResponse;
import br.com.foursys.agendamento_salas.security.AuthenticatedUser;
import br.com.foursys.agendamento_salas.security.SecurityUtils;
import br.com.foursys.agendamento_salas.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SecurityUtils securityUtils;

    public AgendamentoController(AgendamentoService agendamentoService, SecurityUtils securityUtils) {
        this.agendamentoService = agendamentoService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<ConfimacaoAgendamentoResponse> create(
            @RequestBody CriarAgendamentoRequest agendamentoRequest
    ) {

        Long usuarioAutenticado = securityUtils.get().id();

        ConfimacaoAgendamentoResponse response = agendamentoService.create(agendamentoRequest, usuarioAutenticado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AgendamentoResponse>> buscaMeusAgendamentos() {
        Long usuarioAutenticado = securityUtils.get().id();

        List<AgendamentoResponse> listaAgendamentos = agendamentoService.buscaAgendamentos(usuarioAutenticado);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listaAgendamentos);
    }

    @GetMapping("/busca/todos")
    public ResponseEntity<List<AgendamentoResponse>> buscaAgendamentos() {
        Long usuarioAutenticado = securityUtils.get().id();

        List<AgendamentoResponse> listaAgendamentos = agendamentoService.buscaTodosAgendamentos(usuarioAutenticado);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listaAgendamentos);
    }
}
