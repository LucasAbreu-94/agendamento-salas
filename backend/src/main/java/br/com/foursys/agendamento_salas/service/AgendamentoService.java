package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.exception.*;
import br.com.foursys.agendamento_salas.repository.AgendamentoRepository;
import br.com.foursys.agendamento_salas.repository.LogRepository;
import br.com.foursys.agendamento_salas.repository.SalaRepository;
import br.com.foursys.agendamento_salas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final LogRepository logRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, SalaRepository salaRepository, LogRepository logRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public void create(CriarAgendamentoRequest agendamentoRequest, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(UsuarioInexistenteException::new);

        Sala sala = salaRepository.findById(agendamentoRequest.salaId().getId())
                .orElseThrow(SalaInexistenteException::new);

        validarQuantidadeHorasAgendamento(agendamentoRequest.horaInicio(),
                agendamentoRequest.horaFim(), usuario.getPerfilUsuario());

        validarCapacidadeMaximaSala(agendamentoRequest.qntdPessoas(), sala.getCapacidade());

        validarConflitoHorario(sala.getId(), agendamentoRequest.data(), agendamentoRequest.horaInicio(),
                agendamentoRequest.horaFim());

        if(!sala.getDisponivel()) {
            throw new SalaIndisponivelException();
        }

        //Hora inicial anterior à final.
        validarHorarioAgendamento(agendamentoRequest.horaInicio(), agendamentoRequest.horaFim());

        Agendamento agendamento = Agendamento
                .builder()
                .usuarioId(usuario)
                .salaId(sala)
                .dataAgendamento(agendamentoRequest.data())
                .dataCriacao(LocalDateTime.now())
                .horaInicio(agendamentoRequest.horaInicio())
                .horaFim(agendamentoRequest.horaFim())
                .qntdPessoas(agendamentoRequest.qntdPessoas())
                .titulo(agendamentoRequest.titulo())
                .status(StatusAgendamento.CONFIRMADO)
                .ultimaAtualizacao(LocalDateTime.now())
                .build();

        if(agendamentoRequest.usuarioIdSolicitante() != null) {
            if(!usuario.getPerfilUsuario().equals(PerfilUsuario.ADMIN)) {
                throw new UsuarioSemPermissaoException();
            } else {
                usuarioRepository.findById(agendamentoRequest.usuarioIdSolicitante().getId())
                        .orElseThrow(UsuarioInexistenteException::new);

                agendamento.setUsuarioIdSolicitante(agendamentoRequest.usuarioIdSolicitante());
            }
        }

        agendamentoRepository.save(agendamento);

        Log log = Log
                .builder()
                .usuario(usuario)
                .dataAgendamento(agendamento.getDataAgendamento())
                .dataCriacao(agendamento.getDataCriacao())
                .salaAgendamento(agendamento.getSalaId())
                .horaInicioAgendamento(agendamento.getHoraInicio())
                .horaFimAgendamento(agendamento.getHoraFim())
                .build();

        logRepository.save(log);
    }

//  Métodos Utilitarios
    private long calcularDuracao(LocalTime inicio, LocalTime fim) {
        return Duration.between(inicio, fim).toMinutes();
    }

    private void validarQuantidadeHorasAgendamento(LocalTime horaInicio, LocalTime horaFim, PerfilUsuario perfilUsuario) {
        if (calcularDuracao(horaInicio, horaFim) > 120
                && perfilUsuario != PerfilUsuario.ADMIN) {
            throw new ValidacaoBuscaException(
                    "A duração não pode ser maior que 2 horas."
            );
        }
    }

    private void validarCapacidadeMaximaSala(int qtdPessoas, int capacidadeSala) {
        if(qtdPessoas > capacidadeSala) {
            throw new CapacidadeNaoSuportadaException();
        }
    }

    private void validarConflitoHorario(Long idSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim) {
        boolean conflito = logRepository.existeConflito(idSala, dataAgendamento, horaInicio, horaFim);

        if(conflito) {
            throw new SalaIndisponivelException();
        }
    }

    private void validarHorarioAgendamento(LocalTime horaInicio, LocalTime horaFim) {
        if(horaInicio.isAfter(horaFim)) {
            throw new HorarioAgendamentoInvalidoException();
        }
    }
}
