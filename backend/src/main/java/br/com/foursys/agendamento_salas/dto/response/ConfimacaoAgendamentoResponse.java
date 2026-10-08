package br.com.foursys.agendamento_salas.dto.response;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record ConfimacaoAgendamentoResponse (
        Long id,
        Sala sala,
        LocalTime inicio,
        LocalTime fim,
        StatusAgendamento status,
        Usuario solicitante
){

}
