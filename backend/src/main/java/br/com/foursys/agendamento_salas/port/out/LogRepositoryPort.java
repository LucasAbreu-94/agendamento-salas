package br.com.foursys.agendamento_salas.port.out;

import br.com.foursys.agendamento_salas.domain.Log;

import java.time.LocalDate;
import java.time.LocalTime;

public interface LogRepositoryPort {
    Log salvar(Log log);

    Boolean existeConflito(Long salaId, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim);
}
