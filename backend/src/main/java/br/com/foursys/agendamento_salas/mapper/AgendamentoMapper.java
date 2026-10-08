package br.com.foursys.agendamento_salas.mapper;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.dto.response.AgendamentoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {
    AgendamentoResponse entityToResponse(Agendamento entity);
}
