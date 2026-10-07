package br.com.foursys.agendamento_salas.infrastructure.persistence.adapter;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.infrastructure.persistence.repository.SalaJpaRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SalaRepositoryAdapterTest {

    @Test
    void deveDelegarOperacoesDeSalaAoRepositorioJpa() {
        SalaJpaRepository jpaRepository = mock(SalaJpaRepository.class);
        SalaRepositoryAdapter adapter = new SalaRepositoryAdapter(jpaRepository);
        Sala sala = Sala.builder().id(6L).nome("Focus").build();
        LocalDate data = LocalDate.of(2026, 11, 3);
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fim = LocalTime.of(10, 0);
        when(jpaRepository.findAll()).thenReturn(List.of(sala));
        when(jpaRepository.buscarSalasDisponiveis(data, inicio, fim, 5)).thenReturn(List.of(sala));
        when(jpaRepository.findById(6L)).thenReturn(Optional.of(sala));
        when(jpaRepository.save(sala)).thenReturn(sala);
        when(jpaRepository.existsByNomeIgnoreCase("Focus")).thenReturn(true);

        assertEquals(List.of(sala), adapter.buscarTodas());
        assertEquals(List.of(sala), adapter.buscarDisoniveis(data, inicio, fim, 5));
        assertEquals(Optional.of(sala), adapter.buscarPorId(6L));
        assertSame(sala, adapter.salvar(sala));
        assertTrue(adapter.existsByNomeIgnoreCase("Focus"));
        when(jpaRepository.existsByNomeIgnoreCase("Ausente")).thenReturn(false);
        assertFalse(adapter.existsByNomeIgnoreCase("Ausente"));

        verify(jpaRepository).buscarSalasDisponiveis(data, inicio, fim, 5);
        verify(jpaRepository).findById(6L);
        verify(jpaRepository).save(sala);
    }
}
