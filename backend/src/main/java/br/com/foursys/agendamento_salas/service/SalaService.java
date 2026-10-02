package br.com.foursys.agendamento_salas.service;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.domain.Usuario;
import br.com.foursys.agendamento_salas.dto.request.CriarSalaRequest;
import br.com.foursys.agendamento_salas.dto.response.SalaResponse;
import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import br.com.foursys.agendamento_salas.exception.SalaInexistenteException;
import br.com.foursys.agendamento_salas.exception.SalaJaExisteException;
import br.com.foursys.agendamento_salas.exception.UsuarioInexistenteException;
import br.com.foursys.agendamento_salas.exception.ValidacaoBuscaException;
import br.com.foursys.agendamento_salas.mapper.SalaMapper;
import br.com.foursys.agendamento_salas.port.out.SalaRepositoryPort;
import br.com.foursys.agendamento_salas.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalaService {
    private final SalaRepositoryPort salaRepositoryPort;
    private final SalaMapper salaMapper;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public SalaService(SalaRepositoryPort salaRepositoryPort, SalaMapper salaMapper, UsuarioRepositoryPort usuarioRepositoryPort) {
        this.salaRepositoryPort = salaRepositoryPort;
        this.salaMapper = salaMapper;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public SalaResponse criar(CriarSalaRequest request){
        String nomeNormalizado = normalizarNome(request.nome());

        if (salaRepositoryPort.existsByNomeIgnoreCase(nomeNormalizado)){
            throw new SalaJaExisteException();
        }

        Sala sala = Sala.builder()
                .nome(request.nome())
                .disponivel(true)
                .capacidade(request.capacidade())
                .localizacao(request.localizacao())
                .build();

        Sala salaSalva = salaRepositoryPort.salvar(sala);

        return salaMapper.entityToResponse(salaSalva);

    }

    public SalaResponse buscarPorId(Long id){
       Sala sala = salaRepositoryPort.buscarPorId(id).orElseThrow(SalaInexistenteException::new);

        return salaMapper.entityToResponse(sala);
    }

    public List<SalaResponse> listarSalas(){
        return salaRepositoryPort.buscarTodas()
                .stream()
                .map(salaMapper::entityToResponse)
                .toList();
    }

    public List<SalaResponse> buscarSalasDisponiveis(LocalDate data, LocalTime inicio, LocalTime fim, Integer pessoas, String email) {
        long duracao = calcularDuracao(inicio,fim);
        Usuario usuario = usuarioRepositoryPort.buscarUsuarioEmail(email).orElseThrow(UsuarioInexistenteException::new);

        PerfilUsuario perfilUsuario = usuario.getPerfilUsuario();

        validar(data, inicio, fim, duracao, pessoas, perfilUsuario);

        return salaRepositoryPort.buscarDisoniveis(data,inicio,fim,pessoas)
                .stream()
                .map(salaMapper::entityToResponse)
                .toList();

    }

    private String normalizarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }

    private void validar(LocalDate data, LocalTime inicio, LocalTime fim, Long duracao, Integer pessoas, PerfilUsuario perfilUsuario) {
        if (pessoas == null || pessoas < 1) {
            throw new ValidacaoBuscaException("A quantidade de pessoas deve ser ao menos 1.");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new ValidacaoBuscaException("A data não pode estar no passado.");
        }
        if (fim == null && duracao == null) {
            throw new ValidacaoBuscaException("Informe o horário final ou a duração da reserva.");
        }
        if (duracao != null && duracao < 1) {
            throw new ValidacaoBuscaException("A duração deve ser de ao menos 1 minuto.");
        }

        if (duracao >2 && perfilUsuario.equals("USER")){
            throw new ValidacaoBuscaException("A duração não pode ser maior que 2 horas");
        }
        LocalTime fimReserva = (fim != null) ? fim : inicio.plusMinutes(duracao);
        if (!fimReserva.isAfter(inicio)) {
            throw new ValidacaoBuscaException("O horário final deve ser posterior ao horário inicial.");
        }
    }

    public long calcularDuracao(LocalTime inicio, LocalTime fim) {
        return Duration.between(inicio, fim).toMinutes();
    }
}
