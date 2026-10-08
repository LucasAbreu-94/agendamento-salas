package br.com.foursys.agendamento_salas.exception;

public class HorarioAgendamentoInvalidoException extends RuntimeException {
    public HorarioAgendamentoInvalidoException() {
        super("Horário de agendamento inválido.");
    }
}
