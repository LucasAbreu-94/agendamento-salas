package br.com.foursys.agendamento_salas.exception;

public class CapacidadeNaoSuportadaException extends RuntimeException {
    public CapacidadeNaoSuportadaException() {
        super("Capacidade máxima da sala ultrapassada.");
    }
}
