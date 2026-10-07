package br.com.foursys.agendamento_salas.mapper;

import br.com.foursys.agendamento_salas.domain.Sala;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SalaMapperTest {

    @Test
    void deveMapearTodosOsCamposDaSalaParaResponse() {
        Sala sala = Sala.builder()
                .id(3L)
                .nome("Sala Focus")
                .disponivel(true)
                .capacidade(8)
                .localizacao("Tamboré")
                .build();

        var response = Mappers.getMapper(SalaMapper.class).entityToResponse(sala);

        assertEquals(3L, response.id());
        assertEquals("Sala Focus", response.nome());
        assertEquals(true, response.disponivel());
        assertEquals(8, response.capacidade());
        assertEquals("Tamboré", response.localizacao());
    }

    @Test
    void deveRetornarNuloQuandoSalaForNula() {
        assertNull(Mappers.getMapper(SalaMapper.class).entityToResponse(null));
    }
}
