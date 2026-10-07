package br.com.foursys.agendamento_salas.controller;

import br.com.foursys.agendamento_salas.dto.response.CampoInterpretado;
import br.com.foursys.agendamento_salas.dto.response.InterpretarAgendamentoResponse;
import br.com.foursys.agendamento_salas.security.JwtAuthenticationFilter;
import br.com.foursys.agendamento_salas.service.OllamaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = OllamaController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class OllamaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OllamaService ollamaService;

    @Test
    void deveInterpretarAgendamentoComSucesso() throws Exception {

        String mensagem =
                "quero uma sala para hoje inicio as 14h e fim as 15h para cinco pessoas";

        InterpretarAgendamentoResponse resposta =
                new InterpretarAgendamentoResponse(
                        new CampoInterpretado<>(
                                LocalDate.of(2026, 10, 7),
                                true
                        ),
                        new CampoInterpretado<>(
                                LocalTime.of(14, 0),
                                true
                        ),
                        new CampoInterpretado<>(
                                LocalTime.of(15, 0),
                                true
                        ),
                        new CampoInterpretado<>(
                                5,
                                true
                        )
                );

        when(ollamaService.interpretarAgendamento(mensagem))
                .thenReturn(resposta);

        mockMvc.perform(
                        get("/api/ollama/interpretar")
                                .param("mensagem", mensagem)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valor").value("2026-10-07"))
                .andExpect(jsonPath("$.data.informado").value(true))
                .andExpect(jsonPath("$.inicio.valor").value("14:00:00"))
                .andExpect(jsonPath("$.inicio.informado").value(true))
                .andExpect(jsonPath("$.fim.valor").value("15:00:00"))
                .andExpect(jsonPath("$.fim.informado").value(true))
                .andExpect(jsonPath("$.quantidadePessoas.valor").value(5))
                .andExpect(jsonPath("$.quantidadePessoas.informado").value(true));

        verify(ollamaService).interpretarAgendamento(mensagem);
    }

    @Test
    void deveRetornarBadRequestQuandoMensagemNaoForInformada()
            throws Exception {

        mockMvc.perform(
                        get("/api/ollama/interpretar")
                )
                .andExpect(status().isBadRequest());
    }
}