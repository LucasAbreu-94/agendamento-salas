package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
