package br.com.foursys.agendamento_salas.repository;

import br.com.foursys.agendamento_salas.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Query("""
    SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
    FROM Log l
    WHERE l.salaAgendamento.id = :salaId
      AND l.dataAgendamento = :dataAgendamento
      AND l.horaInicioAgendamento < :horaFim
      AND l.horaFimAgendamento > :horaInicio
""")
    boolean existeConflito(
            @Param("salaId") Long salaId,
            @Param("dataAgendamento") LocalDate dataAgendamento,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );
}
