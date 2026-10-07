package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.dto.request.OllamaRequest;
import br.com.foursys.agendamento_salas.dto.response.InterpretarAgendamentoResponse;
import br.com.foursys.agendamento_salas.dto.response.OllamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Service
public class OllamaService {

    private final RestClient ollamaRestClient;
    private final String model;
    private final ObjectMapper objectMapper;

    public OllamaService(RestClient ollamaRestClient, @Value("${ollama.model}") String model, ObjectMapper objectMapper) {
        this.ollamaRestClient = ollamaRestClient;
        this.model = model;
        this.objectMapper = objectMapper;
    }

    public String gerarResposta(String prompt) {

        OllamaRequest request = new OllamaRequest(model, prompt, false);

        OllamaResponse response = ollamaRestClient.post().uri("/api/generate").body(request).retrieve().body(OllamaResponse.class);

        return response.response();
    }

    public InterpretarAgendamentoResponse interpretarAgendamento(String mensagem) {

        LocalDate dataAtual = LocalDate.now();

        String prompt = """
                              Você é um assistente responsável por interpretar pedidos de agendamento de salas.
                
                                A data atual é %s.
                
                                Extraia da mensagem:
                                - data
                                - horário inicial
                                - horário final
                                - quantidade de pessoas
                
                                Para CADA campo, informe:
                                - valor: o valor encontrado ou determinado a partir da mensagem
                                - informado: true se o usuário informou esse dado
                                - informado: false se o usuário NÃO informou esse dado
                
                                REGRA MUITO IMPORTANTE:
                                Se o usuário NÃO informar um campo:
                                - "informado" DEVE ser false
                                - "valor" DEVE ser null
                                - NUNCA invente um valor
                                - NUNCA use 00:00 como substituto
                
                                REGRAS PARA INTERPRETAÇÃO DE DATAS:
                
                                - A data atual é %s.
                                - Se o usuário disser "hoje", considere que ele INFORMOU a data.
                                  Nesse caso:
                                  - "informado" deve ser true
                                  - "valor" deve ser exatamente a data atual: %s.
                                - Se o usuário disser "amanhã", considere que ele INFORMOU a data.
                                  Nesse caso:
                                  - "informado" deve ser true
                                  - "valor" deve ser a data do dia seguinte à data atual.
                                - Se o usuário não mencionar nenhuma data, então:
                                  - "informado" deve ser false
                                  - "valor" deve ser null.
                
                                REGRAS PARA HORÁRIOS:
                
                                - "14h" deve ser convertido para "14:00:00".
                                - "14h30" deve ser convertido para "14:30:00".
                                - "14:00" deve ser mantido como "14:00:00".
                                - Não invente horários.
                
                                REGRAS PARA QUANTIDADE:
                
                                - Quantidades escritas por extenso devem ser convertidas para números.
                                - Não invente quantidade de pessoas.
                
                                Retorne SOMENTE JSON válido.
                
                                Use exatamente este formato:
                
                                {
                                  "data": {
                                    "valor": null,
                                    "informado": false
                                  },
                                  "inicio": {
                                    "valor": null,
                                    "informado": false
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
                
                                Mensagem do usuário:
                                %s
                                ""\".formatted(dataAtual, dataAtual, dataAtual, mensagem);
                
                Mensagem do usuário:
                %s
                """.formatted(dataAtual, dataAtual,dataAtual,dataAtual, mensagem);

        try {
            String resposta = gerarResposta(prompt);

            resposta = resposta.replace("```json", "").replace("```", "").trim();

            return objectMapper.readValue(resposta, InterpretarAgendamentoResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao interpretar agendamento com Ollama", e);
        }
    }
}
