package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.domain.Agendamento;
import br.com.foursys.agendamento_salas.domain.Log;
import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.CriarAgendamentoRequest;
import br.com.foursys.agendamento_salas.dto.response.AgendamentoResponse;
import br.com.foursys.agendamento_salas.dto.response.ConfimacaoAgendamentoResponse;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.enums.StatusAgendamento;
import br.com.foursys.agendamento_salas.exception.CapacidadeNaoSuportadaException;
import br.com.foursys.agendamento_salas.exception.HorarioAgendamentoInvalidoException;
import br.com.foursys.agendamento_salas.exception.SalaIndisponivelException;
import br.com.foursys.agendamento_salas.exception.SalaInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioInexistenteException;
import br.com.foursys.agendamento_salas.exception.UsuarioSemPermissaoException;
import br.com.foursys.agendamento_salas.exception.ValidacaoBuscaException;
import br.com.foursys.agendamento_salas.mapper.AgendamentoMapper;
import br.com.foursys.agendamento_salas.repository.AgendamentoRepository;
import br.com.foursys.agendamento_salas.repository.LogRepository;
import br.com.foursys.agendamento_salas.repository.SalaRepository;
import br.com.foursys.agendamento_salas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgendamentoService - Fluxo de criação de agendamento")
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private LogRepository logRepository;
    @Mock
    private AgendamentoMapper agendamentoMapper;

    private AgendamentoService service;

    @BeforeEach
    void setUp() {
        service = new AgendamentoService(
                agendamentoRepository,
                usuarioRepository,
                salaRepository,
                logRepository,
                agendamentoMapper
        );
    }

    @Nested
    @DisplayName("Criação válida")
    class SuccessfulScenarios {

        @Test
        @DisplayName("Deve criar o agendamento e devolver a confirmação do novo fluxo")
        void deveCriarAgendamentoERegistrarLogParaUsuarioAutenticado() {
            Usuario usuario = usuario(7L, PerfilUsuario.USER);
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala, null, LocalTime.of(9, 0), LocalTime.of(11, 0), 8
            );
            prepararCriacao(usuario, sala, request);
            when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(invocation -> {
                Agendamento salvo = invocation.getArgument(0);
                salvo.setId(99L);
                return salvo;
            });

            ConfimacaoAgendamentoResponse confirmacao = service.create(request, 7L);

            ArgumentCaptor<Agendamento> agendamentoCaptor = ArgumentCaptor.forClass(Agendamento.class);
            ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
            verify(agendamentoRepository).save(agendamentoCaptor.capture());
            verify(logRepository).save(logCaptor.capture());

            Agendamento agendamento = agendamentoCaptor.getValue();
            Log log = logCaptor.getValue();
            assertThat(confirmacao.id()).isEqualTo(99L);
            assertThat(confirmacao.status()).isEqualTo(StatusAgendamento.CONFIRMADO);
            assertThat(confirmacao.sala()).isEqualTo(sala);
            assertThat(confirmacao.inicio()).isEqualTo(request.horaInicio());
            assertThat(confirmacao.fim()).isEqualTo(request.horaFim());
            assertThat(agendamento.getUsuarioId()).isEqualTo(usuario);
            assertThat(agendamento.getSalaId()).isEqualTo(sala);
            assertThat(agendamento.getTitulo()).isEqualTo("Alinhamento");
            assertThat(agendamento.getQntdPessoas()).isEqualTo(8);
            assertThat(agendamento.getDataCriacao()).isNotNull();
            assertThat(log.getUsuario()).isEqualTo(usuario);
            assertThat(log.getSalaAgendamento()).isEqualTo(sala);
            assertThat(log.getDataAgendamento()).isEqualTo(request.data());
            assertThat(log.getHoraInicioAgendamento()).isEqualTo(request.horaInicio());
            assertThat(log.getHoraFimAgendamento()).isEqualTo(request.horaFim());
        }

        @Test
        @DisplayName("Deve permitir que um administrador agende por mais de duas horas para outro usuário")
        void deveCriarAgendamentoParaSolicitanteQuandoAdmin() {
            Usuario admin = usuario(7L, PerfilUsuario.ADMIN);
            Usuario solicitante = usuario(8L, PerfilUsuario.USER);
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala,
                    solicitante,
                    LocalTime.of(9, 0),
                    LocalTime.of(12, 0)
            );
            prepararCriacao(admin, sala, request);
            when(usuarioRepository.findById(8L)).thenReturn(Optional.of(solicitante));

            service.create(request, 7L);

            ArgumentCaptor<Agendamento> agendamentoCaptor = ArgumentCaptor.forClass(Agendamento.class);
            verify(agendamentoRepository).save(agendamentoCaptor.capture());
            verify(logRepository).save(any(Log.class));
            assertThat(agendamentoCaptor.getValue().getUsuarioIdSolicitante()).isEqualTo(solicitante);
        }
    }

    @Nested
    @DisplayName("Falhas e validações")
    class ValidationFailures {

        @Test
        @DisplayName("Deve rejeitar o agendamento quando o usuário autenticado não existe")
        void deveRejeitarAgendamentoQuandoUsuarioNaoExiste() {
            when(usuarioRepository.findById(7L)).thenReturn(Optional.empty());

            assertThrows(UsuarioInexistenteException.class,
                    () -> service.create(request(sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0)), 7L));

            verifyNoInteractions(salaRepository, logRepository, agendamentoRepository);
        }

        @Test
        @DisplayName("Deve rejeitar o agendamento quando a sala não existe")
        void deveRejeitarAgendamentoQuandoSalaNaoExiste() {
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
            when(salaRepository.findById(12L)).thenReturn(Optional.empty());

            assertThrows(SalaInexistenteException.class,
                    () -> service.create(request(sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0)), 7L));

            verifyNoInteractions(logRepository, agendamentoRepository);
        }

        @Test
        @DisplayName("Deve rejeitar o agendamento quando houver conflito de horário")
        void deveRejeitarSalaComConflitoDeHorario() {
            CriarAgendamentoRequest request = request(
                    sala(12L), null, LocalTime.of(9, 0), LocalTime.of(10, 0)
            );
            prepararCriacao(usuario(7L, PerfilUsuario.USER), sala(12L), request);
            when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                    .thenReturn(true);

            assertThrows(SalaIndisponivelException.class, () -> service.create(request, 7L));

            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar o agendamento quando a sala estiver indisponível")
        void deveRejeitarSalaIndisponivel() {
            Sala sala = sala(12L, false, 8);
            CriarAgendamentoRequest request = request(
                    sala, null, LocalTime.of(9, 0), LocalTime.of(10, 0)
            );
            prepararCriacao(usuario(7L, PerfilUsuario.USER), sala, request);

            assertThrows(SalaIndisponivelException.class, () -> service.create(request, 7L));

            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar duração superior a duas horas para usuário comum")
        void deveRejeitarDuracaoSuperiorADuasHorasParaUsuarioComum() {
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala, null, LocalTime.of(9, 0), LocalTime.of(11, 1)
            );
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
            when(salaRepository.findById(12L)).thenReturn(Optional.of(sala));

            ValidacaoBuscaException exception = assertThrows(
                    ValidacaoBuscaException.class,
                    () -> service.create(request, 7L)
            );

            assertThat(exception).hasMessage("A duração não pode ser maior que 2 horas.");
            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar quantidade de pessoas acima da capacidade da sala")
        void deveRejeitarQuantidadeDePessoasAcimaDaCapacidade() {
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala, null, LocalTime.of(9, 0), LocalTime.of(10, 0), 9
            );
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));
            when(salaRepository.findById(12L)).thenReturn(Optional.of(sala));

            assertThrows(CapacidadeNaoSuportadaException.class, () -> service.create(request, 7L));

            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar o agendamento quando o horário final anteceder o inicial")
        void deveRejeitarHorarioFinalAnteriorAoInicial() {
            CriarAgendamentoRequest request = request(
                    sala(12L), null, LocalTime.of(10, 0), LocalTime.of(9, 0)
            );
            prepararCriacao(usuario(7L, PerfilUsuario.USER), sala(12L), request);
            when(logRepository.existeConflito(12L, request.data(), request.horaInicio(), request.horaFim()))
                    .thenReturn(false);

            assertThrows(HorarioAgendamentoInvalidoException.class, () -> service.create(request, 7L));

            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve impedir usuário comum de agendar para outra pessoa")
        void deveImpedirUsuarioComumDeAgendarParaOutroUsuario() {
            Usuario solicitante = usuario(8L, PerfilUsuario.USER);
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala, solicitante, LocalTime.of(9, 0), LocalTime.of(10, 0)
            );
            prepararCriacao(usuario(7L, PerfilUsuario.USER), sala, request);

            assertThrows(UsuarioSemPermissaoException.class, () -> service.create(request, 7L));

            verify(usuarioRepository, never()).findById(8L);
            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar solicitante inexistente mesmo quando o agendamento é feito por administrador")
        void deveRejeitarSolicitanteInexistenteMesmoQuandoAdmin() {
            Usuario admin = usuario(7L, PerfilUsuario.ADMIN);
            Sala sala = sala(12L);
            CriarAgendamentoRequest request = request(
                    sala,
                    usuario(8L, PerfilUsuario.USER),
                    LocalTime.of(9, 0),
                    LocalTime.of(10, 0)
            );
            prepararCriacao(admin, sala, request);
            when(usuarioRepository.findById(8L)).thenReturn(Optional.empty());

            assertThrows(UsuarioInexistenteException.class, () -> service.create(request, 7L));

            verify(agendamentoRepository, never()).save(any());
            verify(logRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Consulta de agendamentos")
    class BookingQueries {

        @Test
        @DisplayName("Deve retornar os agendamentos do usuário autenticado")
        void deveRetornarAgendamentosDoUsuarioAutenticado() {
            Usuario usuario = usuario(7L, PerfilUsuario.USER);
            Agendamento agendamento = agendamento(12L);
            AgendamentoResponse responseEsperada = agendamentoResponse();
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.findByUsuarioId_Id(7L)).thenReturn(List.of(agendamento));
            when(agendamentoMapper.entityToResponse(agendamento)).thenReturn(responseEsperada);

            List<AgendamentoResponse> resultado = service.buscaAgendamentos(7L);

            assertThat(resultado).containsExactly(responseEsperada);
            verify(agendamentoRepository).findByUsuarioId_Id(7L);
            verify(agendamentoMapper).entityToResponse(agendamento);
            verifyNoMoreInteractions(agendamentoRepository, agendamentoMapper);
        }

        @Test
        @DisplayName("Deve rejeitar a consulta pessoal quando o usuário autenticado não existe")
        void deveRejeitarConsultaPessoalQuandoUsuarioNaoExiste() {
            when(usuarioRepository.findById(7L)).thenReturn(Optional.empty());

            assertThrows(UsuarioInexistenteException.class, () -> service.buscaAgendamentos(7L));

            verifyNoInteractions(agendamentoRepository, agendamentoMapper);
        }

        @Test
        @DisplayName("Deve retornar todos os agendamentos quando a consulta for feita por administrador")
        void deveRetornarTodosAgendamentosParaAdministrador() {
            Usuario admin = usuario(7L, PerfilUsuario.ADMIN);
            Agendamento primeiro = agendamento(12L);
            Agendamento segundo = agendamento(13L);
            AgendamentoResponse primeiraResposta = agendamentoResponse();
            AgendamentoResponse segundaResposta = agendamentoResponse();
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(admin));
            when(agendamentoRepository.findAll()).thenReturn(List.of(primeiro, segundo));
            when(agendamentoMapper.entityToResponse(primeiro)).thenReturn(primeiraResposta);
            when(agendamentoMapper.entityToResponse(segundo)).thenReturn(segundaResposta);

            List<AgendamentoResponse> resultado = service.buscaTodosAgendamentos(7L);

            assertThat(resultado).containsExactly(primeiraResposta, segundaResposta);
            verify(agendamentoRepository).findAll();
            verify(agendamentoMapper).entityToResponse(primeiro);
            verify(agendamentoMapper).entityToResponse(segundo);
        }

        @Test
        @DisplayName("Deve rejeitar consulta geral quando o usuário não for administrador")
        void deveRejeitarConsultaGeralParaUsuarioComum() {
            when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario(7L, PerfilUsuario.USER)));

            assertThrows(UsuarioSemPermissaoException.class, () -> service.buscaTodosAgendamentos(7L));

            verifyNoInteractions(agendamentoRepository, agendamentoMapper);
        }

        @Test
        @DisplayName("Deve rejeitar consulta geral quando o usuário autenticado não existe")
        void deveRejeitarConsultaGeralQuandoUsuarioNaoExiste() {
            when(usuarioRepository.findById(7L)).thenReturn(Optional.empty());

            assertThrows(UsuarioInexistenteException.class, () -> service.buscaTodosAgendamentos(7L));

            verifyNoInteractions(agendamentoRepository, agendamentoMapper);
        }
    }

    private void prepararCriacao(
            Usuario usuario,
            Sala sala,
            CriarAgendamentoRequest request
    ) {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(sala.getId())).thenReturn(Optional.of(sala));
        when(logRepository.existeConflito(sala.getId(), request.data(), request.horaInicio(), request.horaFim()))
                .thenReturn(false);
    }

    private Usuario usuario(Long id, PerfilUsuario perfil) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setPerfilUsuario(perfil);
        return usuario;
    }

    private Sala sala(Long id) {
        return sala(id, true, 8);
    }

    private Sala sala(Long id, boolean disponivel, int capacidade) {
        return Sala.builder()
                .id(id)
                .nome("Sala 1")
                .disponivel(disponivel)
                .capacidade(capacidade)
                .build();
    }

    private Agendamento agendamento(Long id) {
        return Agendamento.builder()
                .id(id)
                .usuarioId(usuario(7L, PerfilUsuario.USER))
                .salaId(sala(12L))
                .dataAgendamento(LocalDate.now().plusDays(1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFim(LocalTime.of(10, 0))
                .qntdPessoas(4)
                .titulo("Alinhamento")
                .status(StatusAgendamento.CONFIRMADO)
                .build();
    }

    private AgendamentoResponse agendamentoResponse() {
        return new AgendamentoResponse(
                sala(12L),
                LocalDate.now().plusDays(1),
                LocalDate.now().atStartOfDay(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                4,
                "Alinhamento",
                null,
                StatusAgendamento.CONFIRMADO
        );
    }

    private CriarAgendamentoRequest request(
            Sala sala,
            Usuario solicitante,
            LocalTime inicio,
            LocalTime fim
    ) {
        return request(sala, solicitante, inicio, fim, 4);
    }

    private CriarAgendamentoRequest request(
            Sala sala,
            Usuario solicitante,
            LocalTime inicio,
            LocalTime fim,
            int quantidadePessoas
    ) {
        return new CriarAgendamentoRequest(
                sala,
                LocalDate.now().plusDays(1),
                inicio,
                fim,
                quantidadePessoas,
                solicitante,
                "Alinhamento"
        );
    }
}
