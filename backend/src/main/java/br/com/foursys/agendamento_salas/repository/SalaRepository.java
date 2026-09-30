package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}
