package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.domain.SalaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<SalaEntity, Long> {
}
