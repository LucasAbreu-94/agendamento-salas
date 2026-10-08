package br.com.foursys.agendamento_salas.infrastructure.persistence.adapter;

import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.infrastructure.persistence.repository.LogJpaRepository;
import br.com.foursys.agendamento_salas.port.out.LogRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class LogRepositoryAdapter implements LogRepositoryPort {
    private final LogJpaRepository logJpaRepository;

    public LogRepositoryAdapter(LogJpaRepository logJpaRepository) {
        this.logJpaRepository = logJpaRepository;
    }

    @Override
    public Log salvar(Log log) {
        return logJpaRepository.save(log);
    }

    @Override
    public Boolean existeConflito(Long salaId, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim) {
        return logJpaRepository.existeConflito(salaId, dataAgendamento, horaInicio, horaFim);
    }
}