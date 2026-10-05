package br.com.foursys.agendamento_salas.exception;

public class SalaJaExisteException extends RuntimeException {
    public SalaJaExisteException() {

        super("A sala já existe");
    }
}
