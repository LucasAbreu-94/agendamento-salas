package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioId_Id(Long id);
}
