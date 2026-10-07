package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.exception.HorarioAgendamentoInvalidoException;
import br.com.foursys.agendamento_salas.exception.SalaIndisponivelException;
import br.com.foursys.agendamento_salas.exception.SalaInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioSemPermissaoException;
import br.com.foursys.agendamento_salas.repository.AgendamentoRepository;
import br.com.foursys.agendamento_salas.repository.LogRepository;
import br.com.foursys.agendamento_salas.repository.SalaRepository;
import br.com.foursys.agendamento_salas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private LogRepository logRepository;

    private AgendamentoService service;

    @BeforeEach
    void setUp() {
        service = new AgendamentoService(
                agendamentoRepository,
                usuarioRepository,
                salaRepository,
                logRepository
        );
    }

    @Test
    void deveCriarAgendamentoERegistrarLogParaUsuarioAutenticado() {
        Usuario usuario = usuario(7L, PerfilUsuario.USER);
        Sala sala = sala(12L);
        CriarAgendamentoRequest request = request(sala, null, LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);

        service.create(request, 7L);

        ArgumentCaptor<Agendamento> agendamentoCaptor = ArgumentCaptor.forClass(Agendamento.class);
        ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
        verify(agendamentoRepository).save(agendamentoCaptor.capture());
        verify(logRepository).save(logCaptor.capture());
        assertEquals(usuario, agendamentoCaptor.getValue().getUsuarioId());
        assertEquals(sala, agendamentoCaptor.getValue().getSalaId());
        assertEquals(StatusAgendamento.CONFIRMADO, agendamentoCaptor.getValue().getStatus());
        assertEquals("Alinhamento", agendamentoCaptor.getValue().getTitulo());
        assertEquals(usuario, logCaptor.getValue().getUsuario());
        assertEquals(sala, logCaptor.getValue().getSalaAgendamento());
        assertEquals(request.data(), logCaptor.getValue().getDataAgendamento());
    }

    @Test
    void deveCriarAgendamentoParaSolicitanteQuandoAdmin() {
        Usuario admin = usuario(7L, PerfilUsuario.ADMIN);
        Usuario solicitante = usuario(8L, PerfilUsuario.USER);
        Sala sala = sala(12L);
        CriarAgendamentoRequest request = request(
                sala,
                solicitante,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(admin));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);
        when(usuarioRepository.findById(8L)).thenReturn(Optional.of(solicitante));

        service.create(request, 7L);

        ArgumentCaptor<Agendamento> agendamentoCaptor = ArgumentCaptor.forClass(Agendamento.class);
        verify(agendamentoRepository).save(agendamentoCaptor.capture());
        assertEquals(solicitante, agendamentoCaptor.getValue().getUsuarioIdSolicitante());
    }

    @Test
    void deveRejeitarAgendamentoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class,
                () -> service.create(request(sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0)), 7L));

        verifyNoInteractions(salaRepository, logRepository, agendamentoRepository);
    }

    @Test
    void deveRejeitarAgendamentoQuandoSalaNaoExiste() {
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
        when(salaRepository.findById(12L)).thenReturn(Optional.empty());

        assertThrows(SalaInexistenteException.class,
                () -> service.create(request(sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0)), 7L));

        verifyNoInteractions(logRepository, agendamentoRepository);
    }

    @Test
    void deveRejeitarSalaComConflitoDeHorario() {
        CriarAgendamentoRequest request = request(sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala(12L)));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(true);

        assertThrows(SalaIndisponivelException.class, () -> service.create(request, 7L));

        verify(agendamentoRepository, never()).save(any());
        verify(logRepository, never()).save(any());
    }

    @Test
    void deveRejeitarHorarioFinalAnteriorAoInicial() {
        CriarAgendamentoRequest request = request(sala(12L), null, LocalTime.of(10, 0), LocalTime.of(9, 0));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala(12L)));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);

        assertThrows(HorarioAgendamentoInvalidoException.class, () -> service.create(request, 7L));

        verify(agendamentoRepository, never()).save(any());
        verify(logRepository, never()).save(any());
    }

    @Test
    void deveImpedirUsuarioComumDeAgendarParaOutroUsuario() {
        Usuario solicitante = usuario(8L, PerfilUsuario.USER);
        CriarAgendamentoRequest request = request(
                sala(12L),
                solicitante,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala(12L)));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);

        assertThrows(UsuarioSemPermissaoException.class, () -> service.create(request, 7L));

        verify(usuarioRepository, never()).findById(8L);
        verify(agendamentoRepository, never()).save(any());
        verify(logRepository, never()).save(any());
    }

    @Test
    void deveRejeitarSolicitanteInexistenteMesmoQuandoAdmin() {
        Usuario admin = usuario(7L, PerfilUsuario.ADMIN);
        Usuario solicitante = usuario(8L, PerfilUsuario.USER);
        CriarAgendamentoRequest request = request(
                sala(12L),
                solicitante,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(admin));
        when(salaRepository.findById(12L)).thenReturn(Optional.of(sala(12L)));
        when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);
        when(usuarioRepository.findById(8L)).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> service.create(request, 7L));

        verify(agendamentoRepository, never()).save(any());
        verify(logRepository, never()).save(any());
    }

    private Usuario usuario(Long id, PerfilUsuario perfil) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setPerfilUsuario(perfil);
        return usuario;
    }

    private Sala sala(Long id) {
        return Sala.builder().id(id).nome("Sala 1").build();
    }

    private CriarAgendamentoRequest request(
            Sala sala,
            Usuario solicitante,
            LocalTime inicio,
            LocalTime fim
    ) {
        return new CriarAgendamentoRequest(
                sala,
                LocalDate.now().plusDays(1),
                inicio,
                fim,
                4,
                solicitante,
                "Alinhamento"
        );
    }
}
