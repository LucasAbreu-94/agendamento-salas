package br.com.foursys.agendamento_salas.infrastructure.persistence.adapter;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.infrastructure.persistence.repository.AgendamentoJpaRepository;
import br.com.foursys.agendamento_salas.infrastructure.persistence.specification.AgendamentoSpecification;
import br.com.foursys.agendamento_salas.port.out.AgendamentoRepositoryPort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    public List<Agendamento> buscarFiltrados(
            Long usuarioId,
            LocalDate dataInicio,
            LocalDate dataFim,
            StatusAgendamento status) {

        Specification<Agendamento> specification =
                AgendamentoSpecification.filtros(
                        usuarioId,
                        dataInicio,
                        dataFim,
                        status
                );

        return agendamentoJpaRepository.findAll(specification);
    }
}