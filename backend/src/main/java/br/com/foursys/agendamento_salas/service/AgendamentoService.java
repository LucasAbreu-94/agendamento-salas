package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.dto.response.AgendamentoResponse;
import br.com.foursys.agendamento_salas.dto.response.ConfimacaoAgendamentoResponse;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.exception.*;
import br.com.foursys.agendamento_salas.mapper.AgendamentoMapper;
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
import java.util.List;


@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final LogRepository logRepository;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, SalaRepository salaRepository, LogRepository logRepository, AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.logRepository = logRepository;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Transactional
    public ConfimacaoAgendamentoResponse create(CriarAgendamentoRequest agendamentoRequest, Long idUsuario) {
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

        return ConfimacaoAgendamentoResponse.builder()
                .id(agendamento.getId())
                .sala(agendamento.getSalaId())
                .inicio(agendamento.getHoraInicio())
                .fim(agendamento.getHoraFim())
                .status(agendamento.getStatus())
                .solicitante(agendamento.getUsuarioIdSolicitante())
                .build();
    }

    public List<AgendamentoResponse> buscaAgendamentos(Long usuarioAutenticado) {
        usuarioRepository.findById(usuarioAutenticado)
                .orElseThrow(UsuarioInexistenteException::new);

        List<Agendamento> listaAgendamento = agendamentoRepository.findByUsuarioId_Id(usuarioAutenticado);

        return listaAgendamento
                .stream()
                .map(agendamentoMapper::entityToResponse)
                .toList();
    }

    //Somente ADMIN
    public List<AgendamentoResponse> buscaTodosAgendamentos(Long usuarioAutenticado) {
        Usuario usuario = usuarioRepository.findById(usuarioAutenticado)
                .orElseThrow(UsuarioInexistenteException::new);

        if(usuario.getPerfilUsuario() != PerfilUsuario.ADMIN) {
            throw new UsuarioSemPermissaoException();
        }

        List<Agendamento> listaAgendamento = agendamentoRepository.findAll();

        return listaAgendamento
                .stream()
                .map(agendamentoMapper::entityToResponse)
                .toList();
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
