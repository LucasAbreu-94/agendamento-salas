package br.com.foursys.agendamento_salas.dto.response;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AgendamentoResponse(
        Sala salaId,
        LocalDate dataAgendamento,
        LocalDateTime dataCriacao,
        LocalTime horaInicio,
        LocalTime horaFim,
        Integer qntdPessoas,
        String titulo,
        Usuario usuarioIdSolicitante,
        StatusAgendamento status
) {
}
