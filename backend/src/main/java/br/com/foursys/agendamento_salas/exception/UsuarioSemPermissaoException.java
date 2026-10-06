package br.com.foursys.agendamento_salas.exception;

public class UsuarioSemPermissaoException extends RuntimeException {
    public UsuarioSemPermissaoException() {
        super("O usuário não tem pemissão para realizar essa ação.");
    }
}
