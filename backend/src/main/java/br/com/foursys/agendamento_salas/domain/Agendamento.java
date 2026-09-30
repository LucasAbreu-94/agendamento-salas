package br.com.foursys.agendamento_salas.domain;

import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private SalaEntity salaId;

    private LocalDateTime dataAgendamento;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Integer qntdPessoas;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "usuario_id_solicitante", nullable = true)
    private Usuario usuarioIdSolicitante;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    private LocalDateTime ultimaAtualizacao;
}
