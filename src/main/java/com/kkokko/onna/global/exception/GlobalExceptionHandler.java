package com.kkokko.onna.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

	// JSON 깨짐, 필수 파라미터·파일 누락, 타입 불일치, 파라미터 검증 실패, 업로드 용량 초과도 같은 형식으로 400을 준다.
	@ExceptionHandler({
			HttpMessageNotReadableException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			MethodArgumentTypeMismatchException.class,
			HandlerMethodValidationException.class,
			MaxUploadSizeExceededException.class
	})
	public ResponseEntity<ErrorDto> handleBadRequest(Exception e) {
		return toResponse(ErrorCode.INVALID_INPUT);
	}

	private ResponseEntity<ErrorDto> toResponse(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorDto.from(errorCode));
	}
}
