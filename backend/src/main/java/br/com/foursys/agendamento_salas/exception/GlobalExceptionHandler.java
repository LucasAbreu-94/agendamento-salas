package br.com.foursys.agendamento_salas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.foursys.agendamento_salas.dto.response.ErroResposta;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ValidacaoBuscaException.class)
	public ResponseEntity<ErroResposta> validarBusca(ValidacaoBuscaException ex) {
		return ResponseEntity.badRequest().body(new ErroResposta(400, ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResposta> erroInesperado(Exception ex) {
		if (ex instanceof ErrorResponse resposta) {
			int status = resposta.getStatusCode().value();
			String detalhe = resposta.getBody().getDetail();
			String mensagem = (detalhe != null && !detalhe.isBlank()) ? detalhe : "Erro na requisição.";
			return ResponseEntity.status(status).body(new ErroResposta(status, mensagem));
		}
		log.error("Erro inesperado ao processar a requisição", ex);
		return ResponseEntity.internalServerError().body(new ErroResposta(500, "Erro interno inesperado."));
	}
}
