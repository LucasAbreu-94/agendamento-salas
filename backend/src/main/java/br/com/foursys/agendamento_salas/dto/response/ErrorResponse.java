package br.com.foursys.agendamento_salas.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer status,
        String message,
        LocalDateTime timestamp
) {
}
