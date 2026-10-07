package br.com.foursys.agendamento_salas.dto.request;

public record OllamaRequest(
        String model,
        String prompt,
        Boolean stream
) {
}
