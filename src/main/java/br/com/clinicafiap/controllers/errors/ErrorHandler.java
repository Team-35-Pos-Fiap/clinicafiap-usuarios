package br.com.clinicafiap.controllers.errors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.clinicafiap.controllers.response.ErroResponse;
import br.com.clinicafiap.controllers.response.MensagemResponse;
import br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException;
import br.com.clinicafiap.utils.MensagensUtil;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<MensagemResponse> trataNullPointerException(NullPointerException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<MensagemResponse> trataValidacaoCamposException(ValidationException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<MensagemResponse> trataInternalErrorException(InternalServerError e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(UsuarioNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MensagemResponse> trataIllegalArgumentException(IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<MensagemResponse> trataHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
	}

	@ExceptionHandler(NoSuchMessageException.class)
	public ResponseEntity<MensagemResponse> trataNoSuchMessageException(NoSuchMessageException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	protected ResponseEntity<MensagemResponse> getResponse(HttpStatus status, String mensagem) {

		MensagemResponse erroResponse = new ErroResponse(mensagem);
		return ResponseEntity.status(status).body(erroResponse);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<MensagemResponse> trataMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage(), e);

		return getResponse(HttpStatus.BAD_REQUEST, MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PARAMETRO_INVALIDO, e.getName(), e.getRequiredType().getSimpleName()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getAllErrors().forEach(error -> {
			String field = error instanceof FieldError fe ? fe.getField() : "objeto";
			errors.put(field, error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
		Map<String, String> error = new HashMap<>();
		error.put("mensagem", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}