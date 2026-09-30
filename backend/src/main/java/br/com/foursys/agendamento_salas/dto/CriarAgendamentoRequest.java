package br.com.foursys.agendamento_salas.dto;

import br.com.foursys.agendamento_salas.domain.SalaEntity;
import br.com.foursys.agendamento_salas.domain.Usuario;

import java.time.LocalTime;

public record CriarAgendamentoRequest(
        SalaEntity salaId,
        LocalTime horaInicio,
        LocalTime horaFim,
        Integer qntdPessoas,
        Usuario usuarioIdSolicitante,
        String titulo
) {
}
