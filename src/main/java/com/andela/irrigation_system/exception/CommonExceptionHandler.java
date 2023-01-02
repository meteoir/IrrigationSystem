package com.andela.irrigation_system.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> getUser(IllegalStateException e) {
		log.warn("Bad request", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> badRequest(IllegalArgumentException e) {
		log.warn("Bad request", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> notFound(EntityNotFoundException e) {
		log.warn("Bad request", e);
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<String> clientError(HttpClientErrorException e) {
		log.warn("Http client exception", e);
		return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<String> commonException(AuthException e) {
		log.warn("AuthException occurred", e);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> commonException(Exception e) {
		log.warn("Exception occurred", e);
		return ResponseEntity.unprocessableEntity().body(e.getMessage());
	}

	@ExceptionHandler(BasicIrrigationException.class)
	public ResponseEntity<ErrorResponse> errorCoded(BasicIrrigationException e) {
		if (e.getStatus().is5xxServerError()) {
			log.error("handled IrrigationException error", e);
		} else {
			log.warn("handled IrrigationException warning", e);
		}
		return ResponseEntity
				.status(e.getStatus())
				.body(ErrorResponse.withMessage(e.getUserMessage()).setErrorCode(e.getErrorCode()));
	}
}
