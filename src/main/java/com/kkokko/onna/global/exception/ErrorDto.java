package com.kkokko.onna.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {

	private int status;
	private String code;
	private String message;

	public static ErrorDto from(ErrorCode errorCode) {
		return new ErrorDto(errorCode.getStatus().value(), errorCode.name(), errorCode.getMessage());
	}
}
