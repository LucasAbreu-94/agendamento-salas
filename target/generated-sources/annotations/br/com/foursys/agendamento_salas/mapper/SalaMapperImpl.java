package br.com.foursys.agendamento_salas.mapper;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.dto.response.SalaResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-09T11:18:20-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Azul Systems, Inc.)"
)
@Component
public class SalaMapperImpl implements SalaMapper {

    @Override
    public SalaResponse entityToResponse(Sala entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        Boolean disponivel = null;
        Integer capacidade = null;
        String localizacao = null;

        id = entity.getId();
        nome = entity.getNome();
        disponivel = entity.getDisponivel();
        capacidade = entity.getCapacidade();
        localizacao = entity.getLocalizacao();

        SalaResponse salaResponse = new SalaResponse( id, nome, disponivel, capacidade, localizacao );

        return salaResponse;
    }
}
