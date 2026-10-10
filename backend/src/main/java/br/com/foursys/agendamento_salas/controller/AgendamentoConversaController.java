package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.domain.AgendamentoConversa;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.EnviarMensagemRequest;
import br.com.foursys.agendamento_salas.dto.request.SelecionarSalaRequest;
import br.com.foursys.agendamento_salas.dto.response.IniciarConversaResponse;
import br.com.foursys.agendamento_salas.dto.response.OrquestradorAgendamentoResponse;
import br.com.foursys.agendamento_salas.service.OrquestradorAgendamentoService;
import br.com.foursys.agendamento_salas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendamento/conversas")
public class AgendamentoConversaController {

    private final OrquestradorAgendamentoService orquestradorAgendamentoService;
    private final UsuarioService usuarioService;

    public AgendamentoConversaController(OrquestradorAgendamentoService orquestradorAgendamentoService, UsuarioService usuarioService) {
        this.orquestradorAgendamentoService = orquestradorAgendamentoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<IniciarConversaResponse> iniciarConversa(Authentication authentication) {

        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());

        IniciarConversaResponse response = orquestradorAgendamentoService.iniciarConversa(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/mensagens")
    public ResponseEntity<OrquestradorAgendamentoResponse> processarMensagem(
            @RequestBody EnviarMensagemRequest request) {

        OrquestradorAgendamentoResponse response =  orquestradorAgendamentoService.processarMensagem(
                request.conversaId(),
                request.mensagem()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{conversaId}/selecionar")
    public ResponseEntity<OrquestradorAgendamentoResponse> selecionarSala(
            @PathVariable Long conversaId,
            @RequestBody SelecionarSalaRequest request
            ){
        OrquestradorAgendamentoResponse response = orquestradorAgendamentoService.selecionarSala(conversaId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/{conversaId}/confirmar")
    public ResponseEntity<OrquestradorAgendamentoResponse> confirmarAgendamento(
            @PathVariable Long conversaId
    ){
        OrquestradorAgendamentoResponse response = orquestradorAgendamentoService.confirmarAgendamento(conversaId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
