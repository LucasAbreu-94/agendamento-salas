package br.com.foursys.agendamento_salas.exception;

import br.com.foursys.agendamento_salas.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ValidacaoBuscaException.class)
	public ResponseEntity<ErrorResponse> validarBusca(ValidacaoBuscaException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse(400, exception.getMessage(),LocalDateTime.now()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> erroInesperado(Exception ex) {
		if (ex instanceof org.springframework.web.ErrorResponse resposta) {
			int status = resposta.getStatusCode().value();
			String detalhe = resposta.getBody().getDetail();
			String mensagem = (detalhe != null && !detalhe.isBlank()) ? detalhe : "Erro na requisição.";
			return ResponseEntity.status(status).body(new ErrorResponse(status, mensagem, LocalDateTime.now()));
		}
		log.error("Erro inesperado ao processar a requisição", ex);
		return ResponseEntity.internalServerError().body(new ErrorResponse(500, "Erro interno inesperado.", LocalDateTime.now()));
	}

	@ExceptionHandler(UsuarioInexistenteException.class)
	public ResponseEntity<ErrorResponse> handleUsuarioInexistente(
			UsuarioInexistenteException exception
	){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				exception.getMessage(),
				LocalDateTime.now()
		);

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(error);

	}

	@ExceptionHandler(SalaInexistenteException.class)
	public ResponseEntity<ErrorResponse> handleSalaInexistente(
			SalaInexistenteException exception
	) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(
						HttpStatus.NOT_FOUND.value(),
						exception.getMessage(),
						LocalDateTime.now()
				));
	}

	@ExceptionHandler(SalaJaExisteException.class)
	public ResponseEntity<ErrorResponse> handleSalaJaExiste(
			SalaJaExisteException exception
	) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse(
						HttpStatus.CONFLICT.value(),
						exception.getMessage(),
						LocalDateTime.now()
				));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleParametroInexistente() {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(
						HttpStatus.NOT_FOUND.value(),
						"Recurso não encontrado.",
						LocalDateTime.now()
				));
	}

}
