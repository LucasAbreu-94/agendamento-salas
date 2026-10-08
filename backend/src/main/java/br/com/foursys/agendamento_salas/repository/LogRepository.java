package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
