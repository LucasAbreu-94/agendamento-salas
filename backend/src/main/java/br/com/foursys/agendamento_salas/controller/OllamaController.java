package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.dto.response.InterpretarAgendamentoResponse;
import br.com.foursys.agendamento_salas.service.OllamaService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
public class OllamaController {

    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/interpretar")
    public InterpretarAgendamentoResponse interpretar(
            @RequestParam String mensagem
    ) {
        return ollamaService.interpretarAgendamento(mensagem);
    }
}
