package br.com.foursys.agendamento_salas.port.out;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public interface AgendamentoRepositoryPort {
    Agendamento salvar(Agendamento agendamento);

    List<Agendamento> buscarTodos();

    List<Agendamento> buscarUsuarioId(Long id);

    boolean existeConflito(Long salaId, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim);
}
