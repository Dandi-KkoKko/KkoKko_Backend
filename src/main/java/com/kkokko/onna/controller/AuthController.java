package com.kkokko.onna.controller;

import com.kkokko.onna.dto.LoginReqDto;
import com.kkokko.onna.dto.SignupReqDto;
import com.kkokko.onna.dto.SignupResDto;
import com.kkokko.onna.dto.TokenResDto;
import com.kkokko.onna.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "회원가입·로그인·토큰 재발급 (토큰 없이 호출)")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "회원가입", description = """
			비밀번호 8~20자, 닉네임 2~10자.
			- 409 EMAIL_DUPLICATED: 이미 가입한 이메일
			- 400 INVALID_INPUT: 형식·길이 오류""")
	@PostMapping("/signup")
	public ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(req));
	}

	@Operation(summary = "로그인", description = """
			Access(1시간)·Refresh(14일) 토큰을 발급한다. Access는 Authorization: Bearer {token}으로 보낸다.
			- 401 LOGIN_FAILED: 이메일 또는 비밀번호가 틀림""")
	@PostMapping("/login")
	public ResponseEntity<TokenResDto> login(@Valid @RequestBody LoginReqDto req) {
		return ResponseEntity.ok(authService.login(req));
	}
}
