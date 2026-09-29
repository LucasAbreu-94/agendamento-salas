package br.com.foursys.agendamento_salas.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.foursys.agendamento_salas.dto.ErroResposta;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidacaoBuscaException.class)
	public ResponseEntity<ErroResposta> validarBusca(ValidacaoBuscaException ex) {
		return ResponseEntity.badRequest().body(new ErroResposta(400, ex.getMessage()));
	}
}
