package br.com.foursys.agendamento_salas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class OllamaServiceTest {

    private OllamaService ollamaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        ollamaService = spy(
                new OllamaService(
                        null,
                        "llama3.2",
                        objectMapper
                )
        );
    }

    @Test
    void deveInterpretarAgendamentoComTodosOsCampos() {

        String respostaOllama = """
                {
                  "data": {
                    "valor": "2026-10-07",
                    "informado": true
                  },
                  "inicio": {
                    "valor": "14:00:00",
                    "informado": true
                  },
                  "fim": {
                    "valor": "15:00:00",
                    "informado": true
                  },
                  "quantidadePessoas": {
                    "valor": 5,
                    "informado": true
                  }
                }
                """;

        doReturn(respostaOllama)
                .when(ollamaService)
                .gerarResposta(anyString());

        var resposta = ollamaService.interpretarAgendamento(
                "quero uma sala hoje das 14h às 15h para cinco pessoas"
        );

        assertNotNull(resposta);

        assertEquals(
                "2026-10-07",
                resposta.data().valor().toString()
        );

        assertTrue(resposta.data().informado());

        assertEquals(
                "14:00",
                resposta.inicio().valor().toString()
        );

        assertTrue(resposta.inicio().informado());

        assertEquals(
                "15:00",
                resposta.fim().valor().toString()
        );

        assertTrue(resposta.fim().informado());

        assertEquals(
                5,
                resposta.quantidadePessoas().valor()
        );

        assertTrue(resposta.quantidadePessoas().informado());
    }

    @Test
    void deveManterCamposNaoInformadosComoNull() {

        String respostaOllama = """
                {
                  "data": {
                    "valor": null,
                    "informado": false
                  },
                  "inicio": {
                    "valor": "14:00:00",
                    "informado": true
                  },
                  "fim": {
                    "valor": null,
                    "informado": false
                  },
                  "quantidadePessoas": {
                    "valor": null,
                    "informado": false
                  }
                }
                """;

        doReturn(respostaOllama)
                .when(ollamaService)
                .gerarResposta(anyString());

        var resposta = ollamaService.interpretarAgendamento(
                "quero uma sala às 14h"
        );

        assertNotNull(resposta);

        assertNull(resposta.data().valor());
        assertFalse(resposta.data().informado());

        assertEquals(
                "14:00",
                resposta.inicio().valor().toString()
        );

        assertTrue(resposta.inicio().informado());

        assertNull(resposta.fim().valor());
        assertFalse(resposta.fim().informado());

        assertNull(resposta.quantidadePessoas().valor());
        assertFalse(resposta.quantidadePessoas().informado());
    }

    @Test
    void deveInterpretarDataDeAmanha() {

        String respostaOllama = """
                {
                  "data": {
                    "valor": "2026-10-08",
                    "informado": true
                  },
                  "inicio": {
                    "valor": "14:00:00",
                    "informado": true
                  },
                  "fim": {
                    "valor": "15:00:00",
                    "informado": true
                  },
                  "quantidadePessoas": {
                    "valor": 5,
                    "informado": true
                  }
                }
                """;

        doReturn(respostaOllama)
                .when(ollamaService)
                .gerarResposta(anyString());

        var resposta = ollamaService.interpretarAgendamento(
                "quero uma sala amanhã das 14h às 15h para cinco pessoas"
        );

        assertNotNull(resposta);

        assertEquals(
                "2026-10-08",
                resposta.data().valor().toString()
        );

        assertTrue(resposta.data().informado());

        assertEquals(
                "14:00",
                resposta.inicio().valor().toString()
        );

        assertTrue(resposta.inicio().informado());

        assertEquals(
                "15:00",
                resposta.fim().valor().toString()
        );

        assertTrue(resposta.fim().informado());

        assertEquals(
                5,
                resposta.quantidadePessoas().valor()
        );

        assertTrue(resposta.quantidadePessoas().informado());
    }

    @Test
    void deveLancarExcecaoQuandoOllamaRetornarJsonInvalido() {

        String respostaInvalida = """
                isso não é um JSON válido
                """;

        doReturn(respostaInvalida)
                .when(ollamaService)
                .gerarResposta(anyString());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ollamaService.interpretarAgendamento(
                        "quero uma sala amanhã"
                )
        );

        assertEquals(
                "Erro ao interpretar agendamento com Ollama",
                exception.getMessage()
        );
    }
}