package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.exception.SalaInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioSemPermissaoException;
import br.com.foursys.agendamento_salas.repository.AgendamentoRepository;
import br.com.foursys.agendamento_salas.repository.LogRepository;
import br.com.foursys.agendamento_salas.repository.SalaRepository;
import br.com.foursys.agendamento_salas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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

        Agendamento agendamento = Agendamento
                .builder()
                .usuarioId(usuario)
                .salaId(sala)
                .dataAgendamento(LocalDateTime.now())
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
                .salaAgendamento(agendamento.getSalaId())
                .horaInicioAgendamento(agendamento.getHoraInicio())
                .horaFimAgendamento(agendamento.getHoraFim())
                .build();

        logRepository.save(log);
    }
}
