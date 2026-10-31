package com.kkokko.onna.service;

import com.kkokko.onna.domain.User;
import com.kkokko.onna.dto.SignupReqDto;
import com.kkokko.onna.dto.SignupResDto;
import com.kkokko.onna.global.exception.CustomException;
import com.kkokko.onna.global.exception.ErrorCode;
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
}
