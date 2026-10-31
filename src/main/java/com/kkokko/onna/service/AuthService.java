package com.kkokko.onna.service;

import com.kkokko.onna.domain.User;
import com.kkokko.onna.dto.LoginReqDto;
import com.kkokko.onna.dto.RefreshReqDto;
import com.kkokko.onna.dto.SignupReqDto;
import com.kkokko.onna.dto.SignupResDto;
import com.kkokko.onna.dto.TokenResDto;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
import com.kkokko.onna.global.security.JwtProvider;
import com.kkokko.onna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public SignupResDto signup(SignupReqDto req) {
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
		}
		User user = userRepository.save(User.builder()
				.email(req.getEmail())
				.password(passwordEncoder.encode(req.getPassword()))
				.nickname(req.getNickname())
				.build());
		return SignupResDto.from(user);
	}

	// 이메일이 없는 경우와 비밀번호가 틀린 경우를 구분하지 않는다. 가입된 이메일을 알아낼 수 없게 하기 위해서다.
	public TokenResDto login(LoginReqDto req) {
		User user = userRepository.findByEmail(req.getEmail())
				.filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
				.orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));
		return issueTokens(user);
	}

	// 서명이 맞아도 DB에 저장된 최신 Refresh와 다르면(이미 재발급에 쓴 토큰) 막는다.
	public TokenResDto refresh(RefreshReqDto req) {
		Long userId = jwtProvider.getUserIdFromRefreshToken(req.getRefreshToken());
		User user = userRepository.findById(userId)
				.filter(u -> req.getRefreshToken().equals(u.getRefreshToken()))
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
		return issueTokens(user);
	}

	// Refresh는 사용자당 하나만 저장하므로 새로 발급하면 이전 Refresh는 더 쓸 수 없다.
	private TokenResDto issueTokens(User user) {
		String refreshToken = jwtProvider.createRefreshToken(user.getId());
		user.updateRefreshToken(refreshToken);
		return new TokenResDto(jwtProvider.createAccessToken(user.getId()), refreshToken);
	}
}
