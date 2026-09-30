package br.com.foursys.agendamento_salas.dto;

import br.com.foursys.agendamento_salas.model.Sala;
import br.com.foursys.agendamento_salas.model.Usuario;

import java.time.LocalTime;

public record CriarAgendamentoRequest(
        Sala salaId,
        LocalTime horaInicio,
        LocalTime horaFim,
        Integer qntdPessoas,
        Usuario usuarioIdSolicitante,
        String titulo
) {
}