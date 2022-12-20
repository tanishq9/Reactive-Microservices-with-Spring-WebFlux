package com.example.HelloSpringWebFlux.exceptionhandler;

import com.example.HelloSpringWebFlux.dto.InputFailedValidationResponse;
import com.example.HelloSpringWebFlux.exception.InputValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InputValidationHandler {

	@ExceptionHandler(InputValidationException.class)
	public ResponseEntity<InputFailedValidationResponse> handleException(InputValidationException inputValidationException) {
		InputFailedValidationResponse response = new InputFailedValidationResponse();
		response.setErrorCode(inputValidationException.getErrorCode());
		response.setMessage(inputValidationException.getMessage());
		return ResponseEntity.badRequest().body(response);
	}
}
