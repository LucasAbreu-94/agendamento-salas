package br.com.foursys.agendamento_salas.dto.response;

public record CampoInterpretado<T>(
        T valor,
        boolean informado
) {
}
