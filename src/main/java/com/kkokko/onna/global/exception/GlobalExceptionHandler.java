package com.kkokko.onna.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
		return toResponse(e.getErrorCode());
	}

	// @Valid 실패 (JSON, multipart 모두)
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorDto> handleValidException(BindException e) {
		return toResponse(ErrorCode.INVALID_INPUT);
	}

	private ResponseEntity<ErrorDto> toResponse(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorDto.from(errorCode));
	}
}
