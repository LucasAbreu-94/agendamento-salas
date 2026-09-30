package br.com.foursys.agendamento_salas.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataAgendamento;

    @ManyToOne
    @JoinColumn(name = "sala_agendamento")
    private Sala salaAgendamento;

    private LocalTime horaInicioAgendamento;
    private LocalTime horaFimAgendamento;
}
