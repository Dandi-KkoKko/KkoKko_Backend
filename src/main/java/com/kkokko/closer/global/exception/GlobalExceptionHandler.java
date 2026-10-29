package com.kkokko.closer.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
		return toResponse(e.getErrorCode());
	}

	// @Valid 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDto> handleValidException(MethodArgumentNotValidException e) {
		return toResponse(ErrorCode.INVALID_INPUT);
	}

	private ResponseEntity<ErrorDto> toResponse(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorDto.from(errorCode));
	}
}
