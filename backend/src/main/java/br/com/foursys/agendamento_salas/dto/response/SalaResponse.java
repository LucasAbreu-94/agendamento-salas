package br.com.foursys.agendamento_salas.dto.response;

public record SalaResponse(
        Long id,
        String nome,
        Boolean disponivel,
        Integer capacidade,
        String localizacao
) {

}
