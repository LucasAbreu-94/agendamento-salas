package br.com.foursys.agendamento_salas.dto.request;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;

public record CriarAgendamentoRequest(
        Sala salaId,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        Integer qntdPessoas,
        Usuario usuarioIdSolicitante,
        String titulo
) {
}
