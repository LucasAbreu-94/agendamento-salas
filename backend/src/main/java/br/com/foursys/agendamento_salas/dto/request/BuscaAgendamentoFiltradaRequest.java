package br.com.foursys.agendamento_salas.dto.request;

import br.com.foursys.agendamento_salas.enums.StatusAgendamento;

import java.time.LocalDate;

public record BuscaAgendamentoFiltradaRequest(
        LocalDate dataInicio,
        LocalDate dataFinal,
        StatusAgendamento statusAgendamento,
        Boolean todosUsuarios
) {
}
