package com.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = { ValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleValidationException(ValidationException exception) {
		log.error(exception.getMessage(), exception);
		return ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message("Validation error: " + exception.getMessage()).build();
	}

	@ResponseBody
	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDTO handleException(Exception exception) {
		log.error(exception.getMessage(), exception);
		return ErrorDTO.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).message("Unexpected error")
				.build();
	}

}
