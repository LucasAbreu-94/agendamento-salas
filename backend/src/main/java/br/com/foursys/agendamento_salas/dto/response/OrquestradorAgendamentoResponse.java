package br.com.foursys.agendamento_salas.dto.response;

import br.com.foursys.agendamento_salas.enums.StatusAgendamento;

import java.util.List;

public record OrquestradorAgendamentoResponse(
        Long conversaId,
        StatusAgendamento estado,
        String mensagem,
        List<SugestaoSalaResponse> sugestoes
) {
}
