package br.com.foursys.agendamento_salas.dto.request;

public record CriarSalaRequest(
        String nome,
        Integer capacidade,
        String localizacao
) {

}
