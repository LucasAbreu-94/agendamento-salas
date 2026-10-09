package br.com.foursys.agendamento_salas.infrastructure.persistence.repository;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoJpaRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    List<Agendamento> findByUsuarioId_Id(Long id);
}
