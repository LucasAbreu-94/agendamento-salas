package br.com.foursys.agendamento_salas.mapper;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.dto.response.SalaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaMapper {
    SalaResponse entityToResponse(Sala entity);
}
