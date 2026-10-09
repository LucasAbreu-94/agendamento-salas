package br.com.foursys.agendamento_salas.infrastructure.persistence.adapter;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.infrastructure.persistence.repository.AgendamentoJpaRepository;
import br.com.foursys.agendamento_salas.port.out.AgendamentoRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AgendamenRepositorytoAdapter implements AgendamentoRepositoryPort {
    private final AgendamentoJpaRepository agendamentoJpaRepository;

    public AgendamenRepositorytoAdapter(AgendamentoJpaRepository agendamentoJpaRepository) {
        this.agendamentoJpaRepository = agendamentoJpaRepository;
    }

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return agendamentoJpaRepository.save(agendamento);
    }

    @Override
    public List<Agendamento> buscarTodos() {
        return agendamentoJpaRepository.findAll();
    }

    @Override
    public List<Agendamento> buscarUsuarioId(Long id) {
        return agendamentoJpaRepository.findByUsuarioId_Id(id);
    }

    @Override
    public boolean existeConflito(Long salaId, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim) {
        return agendamentoJpaRepository.existeConflito(salaId, dataAgendamento, horaInicio, horaFim);
    }
}
