package com.kkokko.onna.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// common
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

	// auth
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
	EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

	// post
	RETRY_LOCATION(HttpStatus.BAD_REQUEST, "위치 정확도가 낮습니다. 다시 시도해 주세요."),
	NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 기록만 삭제할 수 있습니다."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다."),
	FACE_DETECTED(HttpStatus.UNPROCESSABLE_ENTITY, "사진에 얼굴이 있어 올릴 수 없습니다."),

	// tag
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 태그입니다."),

	// open
	TOO_FAR(HttpStatus.BAD_REQUEST, "사진에 더 가까이 가야 열 수 있습니다."),
	NOT_OPENED(HttpStatus.FORBIDDEN, "열어 본 사진에만 좋아요를 누를 수 있습니다."),
	;

	private final HttpStatus status;
	private final String message;
}
