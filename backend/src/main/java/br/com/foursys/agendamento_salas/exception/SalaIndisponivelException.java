package br.com.foursys.agendamento_salas.exception;

public class SalaIndisponivelException extends RuntimeException {
    public SalaIndisponivelException() {
        super("Sala indispovivel para reserva.");
    }
}
