package br.com.foursys.agendamento_salas.infrastructure.persistence.repository;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoJpaRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioId_Id(Long id);
}
