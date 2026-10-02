package br.com.foursys.agendamento_salas.exception;

public class SalaInexistenteException extends RuntimeException {
    public SalaInexistenteException() {
        super("Sala não encontrada");
    }
}
