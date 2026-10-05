package br.com.foursys.agendamento_salas.exception;

public class UsuarioInexistenteException extends RuntimeException {
    public UsuarioInexistenteException() {
        super("Usuario não encontrado");
    }
}
